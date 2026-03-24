package com.example.test1.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.test1.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class ViewModel5 () : ViewModel(){
    var user = MutableStateFlow(User())

    fun  parsingObeject (parsableObject:String): User {
        var user = User();
        if(parsableObject.isEmpty()){

            return user;
        } else {
            // Parse the JSON string into a User object
            user = Json.decodeFromString<User>(parsableObject)
        }

        return user;
    }
}