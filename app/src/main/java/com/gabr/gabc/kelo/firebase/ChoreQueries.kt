package com.gabr.gabc.kelo.firebase

import com.gabr.gabc.kelo.constants.ChoreFields
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.models.Chore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            if (!ref.exists()) null
            else ref.toObject<Chore>()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Function that retrieves all chores ordered by expiration date
     *
     * @param groupId: group id in which the chores are
     * @return list with all the chores
     * */
    suspend fun getAllChores(groupId: String) : ArrayList<Chore>? {
        return try {
            val ref = instance.collection(fbGroupsCollection).document(groupId)
                .collection(fbChoresSubCollection)
                .orderBy(ChoreFields.expiration, Query.Direction.ASCENDING)
                .get().await()
            val chores = arrayListOf<Chore>()
            ref.documents.forEach { chore ->
                chore.toObject<Chore>()?.let { chores.add(it) }
            }
            chores
        }
        catch (e : Exception) {
            null
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
                user.points += chore.points
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