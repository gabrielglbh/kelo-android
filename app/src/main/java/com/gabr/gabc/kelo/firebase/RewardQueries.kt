package com.gabr.gabc.kelo.firebase

import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.Reward
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/** Class that holds all the necessary Reward Queries to retrieve, read, update or delete chores from Firebase */
class RewardQueries {

    private var instance: FirebaseFirestore = Firebase.firestore
    private val fbGroupsCollection = Constants.fbGroupsCollection
    private val fbRewardsSubCollection = Constants.fbRewardsSubCollection

    /**
     * Function that creates a [Reward] in an existing Group
     *
     * @param reward: reward to be created
     * @param group: group to make the reward in
     * @return Boolean of success
     * */
    suspend fun createReward(reward: Reward, group: Group): Boolean {
        return try {
            instance.collection(fbGroupsCollection).document(group.id)
                .collection(fbRewardsSubCollection)
                .document().set((reward.toMap()))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Function that retrieves a desired [Reward] with a given group id
     *
     * @param rewardId: id to get the reward
     * @param groupId: group id in which the chore is
     * @return [Reward] containing the information
     * */
    suspend fun getReward(rewardId: String, groupId: String): Reward? {
        return null
    }

    /**
     * Function that updates a desired [Reward] with a given group id
     *
     * @param reward: updated reward
     * @param groupId: group id in which the chore is
     * @return Boolean that returns true if query was successful
     * */
    suspend fun updateReward(reward: Reward, groupId: String): Boolean {
        return true
    }

    /**
     * Function that deletes a desired [Reward] with a given group id
     *
     * @param rewardId: ID of the to be deleted reward
     * @param groupId: group id in which the chore is
     * @return Boolean that returns true if query was successful
     * */
    suspend fun deleteReward(rewardId: String, groupId: String): Boolean {
        return true
    }
}