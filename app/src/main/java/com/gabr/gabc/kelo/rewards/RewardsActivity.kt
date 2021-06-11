package com.gabr.gabc.kelo.rewards

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.firebase.RewardQueries
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.viewModels.RewardViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/** Activity that holds all the logic for the Rewards Management */
class RewardsActivity : AppCompatActivity() {

    private lateinit var parent: ConstraintLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var icon: ImageView
    private lateinit var rewardLayout: TextInputLayout
    private lateinit var rewardEdit: TextInputEditText
    private lateinit var rewardPeriodicity: MaterialButton

    private var viewDetails = false

    private lateinit var viewModel: RewardViewModel
    private var reward = Reward()
    private var oldFreq = -1

    companion object {
        const val VIEW_DETAILS = "VIEW_DETAILS"
        const val REWARD = "REWARD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        viewModel = run { ViewModelProvider(this).get(RewardViewModel::class.java) }

        viewDetails = intent.getBooleanExtra(VIEW_DETAILS, false)
        if (viewDetails) {
            reward = intent.getParcelableExtra(REWARD)!!
            oldFreq = reward.frequency
        }

        UtilsSingleton.changeStatusBarColor(this, this, R.color.toolbarBackground)
        toolbar = findViewById(R.id.toolbar_widget)
        if (viewDetails) toolbar.title = getString(R.string.rewards_title_edit)
        else toolbar.title = getString(R.string.rewards_title_new)
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        parent = findViewById(R.id.rewardDetailConstraintLayout)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(this, parent) }

        icon = findViewById(R.id.rewardDetailIcon)
        if (viewDetails) icon.setImageDrawable(UtilsSingleton.createAvatar(reward.name))

        setUpRewardDescription()
        setUpPeriodicityOfReward()

        viewModel.periodicity.observe(this) { mode ->
            reward.frequency = mode.ordinal
            reward.expiration = Reward.Frequencies.getDateFromMode(mode)
            rewardPeriodicity.text = Reward.Frequencies.getStringFromMode(this, mode.ordinal)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu?.findItem(R.id.toolbar_share)?.isVisible = false
        menu?.findItem(R.id.toolbar_completed_chores)?.isVisible = false
        menu?.findItem(R.id.toolbar_information)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.toolbar_done -> {
                validateReward()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun validateReward() {
        reward.name = rewardEdit.text.toString()
        if (!RewardFunctions.isRewardNameValid(rewardEdit.text.toString())) rewardLayout.error = getString(R.string.err_invalid_name)
        else {
            if (reward.expiration == null && reward.frequency != 0) {
                UtilsSingleton.showSnackBar(parent, getString(R.string.err_reward_not_completed))
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    if (viewDetails) {
                        if (oldFreq != reward.frequency) {
                            val mode = Reward.Frequencies.values()[reward.frequency]
                            reward.creation?.let {
                                val creation = Calendar.getInstance()
                                creation.time = it
                                reward.expiration = Reward.Frequencies.getDateFromMode(mode, creation)
                            }
                        }
                        val success = RewardQueries().updateReward(reward, SharedPreferences.groupId)
                        if (success) finish()
                        else UtilsSingleton.showSnackBar(parent, getString(R.string.err_reward_update))
                    } else {
                        reward.creation = Calendar.getInstance().time
                        val reward = RewardQueries().createReward(reward, SharedPreferences.groupId)
                        if (reward != null) finish()
                        else UtilsSingleton.showSnackBar(parent, getString(R.string.err_reward_creation))
                    }
                }
            }
        }
    }

    private fun setUpRewardDescription() {
        rewardLayout = findViewById(R.id.rewardDetailNameLayout)
        rewardEdit = findViewById(R.id.rewardDetailNameEditText)

        if (viewDetails) rewardEdit.setText(reward.name)

        rewardEdit.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) UtilsSingleton.hideKeyboard(this, v)
        }
        rewardEdit.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(rewardLayout) }
        rewardEdit.setOnEditorActionListener { view, _, _ ->
            clearFocusOfEditTextAndSetDrawable()
            UtilsSingleton.hideKeyboard(this, view)
            true
        }
    }

    private fun setUpPeriodicityOfReward() {
        rewardPeriodicity = findViewById(R.id.rewardDetailPeriodicityButton)

        if (viewDetails) rewardPeriodicity.text = Reward.Frequencies.getStringFromMode(this, reward.frequency)

        rewardPeriodicity.setOnClickListener {
            clearFocusOfEditTextAndSetDrawable()
            PeriodicityBottomSheet().show(supportFragmentManager, PeriodicityBottomSheet.TAG)
        }
    }

    private fun clearFocusOfEditTextAndSetDrawable() {
        rewardEdit.clearFocus()
        rewardEdit.text?.let {
            if (it.trim().isNotEmpty()) icon.setImageDrawable(UtilsSingleton.createAvatar(it.toString()))
        }
    }
}