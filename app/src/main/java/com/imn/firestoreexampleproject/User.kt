package com.imn.firestoreexampleproject

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
        var firstName: String? = "",
        var lastName: String? = ""
):Serializable
