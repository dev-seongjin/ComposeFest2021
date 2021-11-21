/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

    //private state
    //mutableState, State, Snapshot에 대해 더 공부해보기
    private var currentEditPosition by mutableStateOf(-1)

    // state: todoItems
    //mutableState와 mutableStateList의 차이 공부 해보기
    var todoItems = mutableStateListOf<TodoItem>()
    private set

    //state
    //이게 어떤 원리로 Observable이 되는지 알아보기
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    // event: addItem
    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    // event: removeItem
    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone() //아이템 삭제시 편집 상태를 유지하면 안됨
    }

    //event: onEditItemSelected
    fun onEditItemSelected(item: TodoItem) {
        //만약 item이 존재하지 않으면 -1이므로 편집 비활성화 상태와 동일
        currentEditPosition = todoItems.indexOf(item)
    }

    //event: onEditDone
    fun onEditDone() {
        currentEditPosition = -1
    }

    //event: onEditItemChange
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }

        todoItems[currentEditPosition] = item
    }
}
