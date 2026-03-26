package com.example.test1.ui.user

import androidx.lifecycle.ViewModel
import com.example.test1.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

class ProfileViewModel () : ViewModel(){
    var user = MutableStateFlow(User())

    fun  parsingObeject (parsableObject:String): User {
        var user = User();
        if(parsableObject.isEmpty()){

            return user;
        } else {
            // Parse the JSON string into a User object
            user = Json.Default.decodeFromString<User>(parsableObject)
        }

        return user;
    }
}