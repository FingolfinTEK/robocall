robocall
========

A REST API for doing text-to-speech calls by leveraging Nuance for text-to-speech conversion and Twilio for calling support. Aside from the message text, language and phone number, the API also allows end-users to control message looping. 

The web service also provides redial functionality in case the call is unsuccessful. The redials will be performed only during the 8:30am - 8:30pm interval (if the call falls outside the interval, it will be rescheduled).
