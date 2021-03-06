package com.mustaq.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: ToDoRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<TodoModel>>
    val ascendingList: LiveData<List<TodoModel>>
    val descendingList: LiveData<List<TodoModel>>


    init {
        val wordsDao = ToDoRoomDataBase.getDatabaseInstance(application).todoDao()
        repository = ToDoRepository(wordsDao)
        allWords = repository.getAllToDoListModel
        ascendingList = repository.getAscendingToDoListModel
        descendingList= repository.getDescendingToDoListModel

    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(toDoDataModel: TodoModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTodo(toDoDataModel)
    }

    fun deleteAll() {
        repository.deleteAllNotes()
    }

    fun getAscendingOrderListViewModel() {
        repository.ascendingOrder()
    }

    fun getDescendingOrderListViewModel() {
        repository.descendingOrder()
    }

    fun deleteSingleItemViewModel(taskId: Int) {
        repository.deleteSingleItem(taskId)
    }


}