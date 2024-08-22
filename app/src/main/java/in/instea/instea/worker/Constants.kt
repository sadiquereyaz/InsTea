/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package `in`.instea.instea.worker

// Name of Notification Channel for verbose notifications of background work
val TASK_NOTIFICATION_CHANNEL_NAME: CharSequence = "Task Reminder"

// Description of Notification Channel for verbose notifications of background work
const val TASK_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever a task reminder is due"

// Title of Notification for verbose notifications of background work
val TASK_NOTIFICATION_TITLE: CharSequence = "Task Reminder"

/*
* User Control: Users can customize or mute channels individually. By categorizing notifications into channels using CHANNEL_ID,
* users have more control over what types of notifications they want to receive.
*Consistency: All notifications that belong to the same channel share the same behavior, ensuring a consistent user experience.
* For example, all reminders for tasks could be grouped under a "Task Reminder" channel, making it easier for users to manage these notifications.
* */
const val TASK_REMINDER_CHANNEL_ID = "REMINDER_NOTIFICATION"

/*Notification ID: This is the unique identifier for a specific notification. It's used to update or cancel the notification later on.
Use: When you issue a notification, you assign it an ID so that if you need to update or remove the notification later,
you can refer to it by this ID. The ID should be unique for each notification or notification group.*/
const val NOTIFICATION_ID = 1

/* Purpose: This is a unique code that identifies the PendingIntent associated with a notification action or other intent-based operation.
Use: When you create a PendingIntent (for example, to open an activity when the user taps on the notification), you provide a request code.
This code helps distinguish between different intents if needed. In this case, it's set to 0, but it could be any unique integer value.*/
const val REQUEST_CODE = 0

