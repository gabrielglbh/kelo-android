package com.gabr.gabc.kelo.firebase

import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.models.Group
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/** Class that holds all the necessary Group Queries to retrieve, read, update or delete groups from Firebase */
class GroupQueries {

    private var instance: FirebaseFirestore = Firebase.firestore
    private val fbGroupsCollection = Constants.fbGroupsCollection
    private val fbUsersCollection = Constants.fbUsersCollection

    /**
     * Function that creates a [Group]
     *
     * @param group: group to be created. If the group ID is empty, Firebase
     * will handle the creation of a random ID. Else, the group ID provided
     * will be used for the group ID in Firebase
     * @return Group containing the newly created group ID
     * */
    suspend fun createGroup(group: Group): Group? {
        return try {
            val ref = if (group.id != "") {
                instance.collection(fbGroupsCollection).document(group.id)
            } else {
                instance.collection(fbGroupsCollection).document()
            }
            ref.set(group).await()
            group.id = ref.id
            group
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Function that retrieves a desired [Group] with a given id
     *
     * @param groupId: id to get the group
     * @return [Group] containing the information
     * */
    suspend fun getGroup(groupId: String): Group? {
        return try {
            val ref = instance.collection(fbGroupsCollection)
                .document(groupId).get()
                .await()
            ref.toObject<Group>()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Function that retrieves a desired [Group]
     *
     * @param group: updated group
     * @return Boolean that returns true if query was successful
     * */
    suspend fun updateGroup(group: Group): Boolean {
        return try {
            instance.collection(fbGroupsCollection)
                .document(group.id).update(group.toMap())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Function that deletes a desired [Group]
     *
     * @param groupId: ID of the to be deleted group
     * @return Boolean that returns true if query was successful
     * */
    suspend fun deleteGroup(groupId: String): Boolean {
        return try {
            instance.collection(fbGroupsCollection)
                .document(groupId).delete()
                .await()
            val users = UserQueries().deleteAllUsers(groupId)
            val chores = ChoreQueries().deleteAllChores(groupId)
            users && chores
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Function that validates the group for a later user to be joined without worrying
     *
     * @param groupId: group ID where the user would like to join
     * @return Int determining the success:
     *      0: no errors
     *      -1: generic error
     *      -2: group is full of users
     *      -3: group does not exist
     * */
    suspend fun checkGroupAvailability(groupId: String): Int {
        return try {
            val group = instance.collection(fbGroupsCollection)
                .document(groupId).get()
                .await()
            if (!group.exists()) -3
            else {
                val obs = instance.collection(fbGroupsCollection)
                        .document(groupId)
                        .collection(fbUsersCollection).get()
                        .await()
                if (obs.size() == 16) -2
                else 0
            }
        } catch (e: Exception) {
            -1
        }
    }
}