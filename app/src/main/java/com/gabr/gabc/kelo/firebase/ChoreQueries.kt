package com.gabr.gabc.kelo.firebase

import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.models.Chore
import com.google.firebase.firestore.DocumentChange.Type.ADDED
import com.google.firebase.firestore.DocumentChange.Type.MODIFIED
import com.google.firebase.firestore.DocumentChange.Type.REMOVED
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/** Class that holds all the necessary Chore Queries to retrieve, read, update or delete chores from Firebase */
class ChoreQueries {

    private var instance: FirebaseFirestore = Firebase.firestore
    private val fbGroupsCollection = Constants.fbGroupsCollection
    private val fbChoresSubCollection = Constants.fbChoresSubCollection

    /**
     * Function that creates a [Chore] in an existing Group
     *
     * @param chore: chore to be created
     * @param groupId: group id in which the chore is
     *
     * If the chore ID is empty, Firebase
     * will handle the creation of a random ID. Else, the group ID provided
     * will be used for the group ID in Firebase
     * @return created Chore
     * */
    suspend fun createChore(chore: Chore, groupId: String): Chore? {
        return try {
            chore.id?.let {
                val ref = if (it != "") {
                    instance.collection(fbGroupsCollection).document(groupId)
                        .collection(fbChoresSubCollection).document(it)
                } else {
                    instance.collection(fbGroupsCollection).document(groupId)
                        .collection(fbChoresSubCollection).document()
                }
                ref.set((chore.toMap())).await()
                chore
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Function that retrieves a desired [Chore] with a given group id
     *
     * @param choreId: id to get the chore
     * @param groupId: group id in which the chore is
     * @return [Chore] containing the information
     * */
    suspend fun getChore(choreId: String, groupId: String): Chore? {
        return try {
            val ref = instance.collection(fbGroupsCollection).document(groupId)
                .collection(fbChoresSubCollection).document(choreId).get().await()
            ref.toObject<Chore>()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Function that defines a listener to the chores of a certain group to display them in the Chore List
     * Depending on the type of the change, the list of chores will update
     *
     * @param groupId: group id in which the chores are
     * @param notifyAdded: function that notifies the recyclerview to update its content
     * @param notifyModified: function that notifies the recyclerview to update its content
     * @param notifyDeleted: function that notifies the recyclerview to update its content
     * @return [ListenerRegistration] of the collection listener
     * */
    fun attachListenerToChores(groupId: String,
                               notifyAdded: (pos: Int, chore: Chore) -> Unit,
                               notifyModified: (pos: Int, chore: Chore) -> Unit,
                               notifyDeleted: (pos: Int) -> Unit) : ListenerRegistration? {
        try {
            val ref = instance.collection(fbGroupsCollection).document(groupId)
                .collection(fbChoresSubCollection)
            return ref.addSnapshotListener { value, e ->
                if (e != null) return@addSnapshotListener
                for (doc in value!!.documentChanges) {
                    val chore = doc.document.toObject<Chore>()
                    when (doc.type) {
                        ADDED -> notifyAdded(doc.newIndex, chore)
                        MODIFIED -> notifyModified(doc.newIndex, chore)
                        REMOVED -> notifyDeleted(doc.oldIndex)
                    }
                }
            }
        }
        catch (e : Exception) {
            return null
        }
    }

    /**
     * Function that updates a desired [Chore] with a given group id
     *
     * @param chore: updated chore
     * @param groupId: group id in which the chore is
     * @return Boolean that returns true if query was successful
     * */
    suspend fun updateChore(chore: Chore, groupId: String): Boolean {
        return try {
            chore.id?.let {
                instance.collection(fbGroupsCollection).document(groupId)
                    .collection(fbChoresSubCollection).document(it)
                    .update(chore.toMap())
                    .await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Function that deletes a desired [Chore] with a given group id
     *
     * @param choreId: ID of the to be deleted chore
     * @param groupId: group id in which the chore is
     * @return Boolean that returns true if query was successful
     * */
    suspend fun deleteChore(choreId: String, groupId: String): Boolean {
        return try {
            instance.collection(fbGroupsCollection).document(groupId)
                .collection(fbChoresSubCollection).document(choreId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Function that completes a desired [Chore] with a given group id
     *
     * @param chore: chore to be completed
     * @param groupId: group id in which the chore is
     * @return Boolean that returns true if query was successful
     * */
    suspend fun completeChore(chore: Chore, groupId: String): Boolean {
        return try {
            val q = UserQueries()
            val user = q.getUser(chore.assignee!!, groupId)
            if (user != null) {
                user.points = chore.points
                val success = q.updateUser(user, groupId)
                if (success) { return deleteChore(chore.id!!, groupId) }
                else false
            } else false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Helper function that deletes all chores in a group
     *
     * @param groupId: group id in which the chores are
     * @return Boolean that returns true if query was successful
     * */
    suspend fun deleteAllChores(groupId: String): Boolean {
        return try {
            val ref = instance.collection(fbGroupsCollection).document(groupId)
                    .collection(fbChoresSubCollection).get().await()
            ref.documents.forEach {
                val chore = it.toObject<Chore>()
                chore?.let { c ->
                    c.id?.let { id -> deleteChore(id, groupId) }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}