package com.gabr.gabc.kelo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.choreDetailActivity.ChoreDetailFunctions
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.choreDetailActivity.AssigneeViewModel
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.*
import com.gabr.gabc.kelo.utils.widgets.CustomDatePicker
import com.gabr.gabc.kelo.choreDetailActivity.UsersBottomSheet
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChoreDetailActivity : AppCompatActivity() {

    companion object {
        const val VIEW_DETAILS = "VIEW_DETAILS"
        const val CHORE = "CHORE"
    }

    private var chore = Chore()
    private lateinit var viewModel: AssigneeViewModel

    private var viewDetails: Boolean = false
    private var selectedCalendar = Calendar.getInstance()

    private lateinit var toolbar: MaterialToolbar

    private lateinit var parent: ConstraintLayout
    private lateinit var icon: ImageView
    private lateinit var nameLayout: TextInputLayout
    private lateinit var nameEditText: TextInputEditText
    private lateinit var date: MaterialButton
    private lateinit var assignee: MaterialButton
    private lateinit var importanceGroup: RadioGroup

    /**
     * Initializes all views for ChoreDetailActivity
     *
     * @param savedInstanceState: current bundle, if any
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chore_detail)

        viewModel = run { ViewModelProvider(this).get(AssigneeViewModel::class.java) }
        viewDetails = intent.getBooleanExtra(VIEW_DETAILS, false)
        if (viewDetails) chore = intent.getParcelableExtra(CHORE)!!

        parent = findViewById(R.id.chore_details_layout)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(this, parent) }

        icon = findViewById(R.id.choreDetailIcon)
        if (viewDetails) icon.setImageDrawable(UtilsSingleton.createAvatar(chore.name))

        setUpToolbar()
        setUpChoreName()
        setUpDatePicker()
        setUpImportance()
        setUpAssignee()
    }

    /**
     * [onCreateOptionsMenu] and [onOptionsItemSelected] manages the creation of the Toolbar
     * [onCreateOptionsMenu] manages the inflation of the Toolbar with the specific menu buttons
     * [onOptionsItemSelected] manages the clicks in the menu
     * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu?.findItem(R.id.toolbar_share)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.toolbar_done -> {
                validateChore()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    /**
     * Sets up the toolbar, enabling the back button automatically
     * */
    private fun setUpToolbar() {
        UtilsSingleton.changeStatusBarColor(this, this, R.color.toolbarBackground)
        toolbar = findViewById(R.id.toolbar_widget)
        toolbar.title = if (viewDetails) getString(R.string.chore_detail) else getString(R.string.add_chore)
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    /**
     * Validates and checks all necessary fields of the Activity and updates the chore to Firebase
     *
     * Depending of the value of [viewDetails] it will INSERT or UPDATE a chore
     * */
    private fun validateChore() {
        val context = this
        chore.name = nameEditText.text.toString()

        chore.name?.let {
            if (!ChoreDetailFunctions.isChoreNameValid(it)) nameLayout.error = getString(R.string.err_invalid_name)
            else {
                if (ChoreDetailFunctions.validateChore(chore)) {
                    SharedPreferences.groupId?.let { id ->
                        CoroutineScope(Dispatchers.Main).launch {
                            if (viewDetails) {
                                val success = ChoreQueries().updateChore(chore, id)
                                if (success) finish()
                                else Toast.makeText(context, R.string.err_chore_update, Toast.LENGTH_SHORT).show()
                            } else {
                                val res = ChoreQueries().createChore(chore, id)
                                if (res != null) finish()
                                else Toast.makeText(context, R.string.err_chore_creation, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.err_chore_not_completed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Function that sets up the chore name
     *
     * If [viewDetails] is true, [nameEditText] has the chore name
     * */
    private fun setUpChoreName() {
        nameLayout = findViewById(R.id.choreDetailNameLayout)
        nameEditText = findViewById(R.id.choreDetailNameEditText)

        if (viewDetails) nameEditText.setText(chore.name)

        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) UtilsSingleton.hideKeyboard(this, v)
        }
        nameEditText.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(nameLayout) }
        nameEditText.setOnEditorActionListener { view, _, _ ->
            doneEditingName()
            UtilsSingleton.hideKeyboard(this, view)
            true
        }
    }

    /**
     * Creates the listener for the DatePicker
     *
     * If [viewDetails] is true, [date] has the chore expiration date, else the current day
     */
    private fun setUpDatePicker() {
        date = findViewById(R.id.choreDetailExpireDateButton)

        if (viewDetails) {
            val c = Calendar.getInstance()
            chore.expiration?.let { c.time = it }
            date.text = UtilsSingleton.parseCalendarToString(c)
        }
        else date.text = UtilsSingleton.parseCalendarToString(Calendar.getInstance())

        date.setOnClickListener {
            doneEditingName()
            val datePicker = CustomDatePicker(selectedCalendar) { day, month, year ->
                onDateSelected(day, month, year)
            }
            datePicker.show(supportFragmentManager, CustomDatePicker.TAG)
        }
    }

    /**
     * Function that is called when a date is selected in the [CustomDatePicker]
     *
     * @param day: selected day
     * @param month: selected month
     * @param year: selected year
     * */
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val calendar = ChoreDetailFunctions.parseAndUpdateChoreWithSelectedDate(chore, day, month, year)
        selectedCalendar = calendar
        date.text = UtilsSingleton.parseCalendarToString(calendar)
    }

    /**
     * Function that controls the chips of importance
     *
     * If [viewDetails] is true, [importanceGroup] checks the importance based on the chore points
     * */
    private fun setUpImportance() {
        importanceGroup = findViewById(R.id.choreDetailImportance)

        when (chore.points) {
            10 -> importanceGroup.check(R.id.choreDetailLow)
            20 -> importanceGroup.check(R.id.choreDetailMedium)
            30 -> importanceGroup.check(R.id.choreDetailHigh)
        }

        importanceGroup.setOnCheckedChangeListener { _, checkedId ->
            doneEditingName()
            when (checkedId) {
                R.id.choreDetailLow -> chore.points = 10
                R.id.choreDetailMedium -> chore.points = 20
                R.id.choreDetailHigh -> chore.points = 30
            }
        }
    }

    /**
     * Function that sets up the assignee button
     *
     * If [viewDetails] is true, [assignee] is the same as the chore assignee
     * */
    private fun setUpAssignee() {
        assignee = findViewById(R.id.choreDetailAssigneeButton)

        if (viewDetails) {
            chore.assignee?.let {
                if (UtilsSingleton.isUserBeingDisplayedCurrentUser(it)) {
                    UtilsSingleton.setTextAndIconToYou(baseContext, assignee, null)
                } else {
                    SharedPreferences.groupId?.let { id ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val username = UserQueries().getUser(it, id)?.name
                            assignee.text = username
                        }
                    }
                }
            }
        }

        assignee.setOnClickListener {
            doneEditingName()
            UsersBottomSheet().show(supportFragmentManager, UsersBottomSheet.TAG)
        }

        observeAssignee()
    }

    /**
     * Sets up the observer for the ViewModel of the [AssigneeViewModel] variable 'assignee'
     * */
    private fun observeAssignee() {
        viewModel.assignee.observe(this, { user ->
            if (UtilsSingleton.isUserBeingDisplayedCurrentUser(user.id)) {
                UtilsSingleton.setTextAndIconToYou(baseContext, assignee, null)
            } else {
                assignee.text = user.name
            }
            chore.assignee = user.id
        })
    }

    /**
     * Clears the focus of the [nameEditText] and sets the new icon for the chore
     * */
    private fun doneEditingName() {
        nameEditText.clearFocus()
        nameEditText.text?.let {
            if (it.trim().isNotEmpty()) icon.setImageDrawable(UtilsSingleton.createAvatar(it.toString()))
        }
    }
}