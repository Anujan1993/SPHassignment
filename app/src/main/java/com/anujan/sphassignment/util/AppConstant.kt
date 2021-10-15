package com.anujan.sphassignment.util

object AppConstant {
    const val NAVIGATE_TO_REGISTER = "NavigateToRegister"
    const val REGISTER_SUCCESS ="Registration Success"
    const val QUEAR = "YEAR"
}
object ErrorObject{
    const val EMAIL_OBJECT = "Email"
    const val NAME_OBJECT = "Name"
    const val USERNAME_OBJECT = "Username"
    const val PHONE_OBJECT = "Phone"
    const val PASSWORD_OBJECT = "Password"
    const val CONFORM_PASSWORD_OBJECT = "Conform Password"
    const val PASSWORD_NOT_MATCH = "Password Not Matched"
}
object ErrorMessages{
    const val SERVER_ERROR = "Error Occurred!"
    const val EMAIL_ERROR = "Please Enter the Correct Email"
    const val PASSWORD_ERROR = "Please Enter the Correct Password"
    const val NAME_ERROR = "Please Enter the Name"
    const val PHONE_ERROR = "Please Enter the Phone Number"
    const val CONFORM_PASSWORD_ERROR = "Please Enter the Conform Password"
    const val PASSWORD_NOT_MATCHED = "Passwords are not matched"
}
object EndPoints {
    const val DATABASE_NAME = "sph.db"
    const val BASE_URL = "https://data.gov.sg/api/action/"
    const val GET_DATA = "datastore_search"
    const val API_KEY = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
}

object SharedPreferencesData{
    const val APP_USER ="APP_USER"
    const val LOGGED_IN = "LoggedIn"
    const val NIGHT_MODE = "Night Mode"
}