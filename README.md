# InsTea v2.0

**Android APK File**: [Download here](https://drive.google.com/file/d/12ffCQKxx6sCXTMJrymFF0YrcG8RHBE6p/view?usp=drivesdk)

## Screenshots
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtube.com/shorts/-FrDlyas8jk?si=hcjyCpQlrpgMzgC1)
---
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/65c84442-6f61-411e-b196-517fc34d0cd4" alt="Image 1" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/cfe04538-1418-4f73-af3e-c2e95f958c26" alt="Image 1" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/dbebec78-ea8c-496f-839e-db438f3eb523" alt="Image 2" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/b300e2ba-e6d3-4414-856d-f0843b978a5e" alt="Image 6" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/ea74acbf-b00d-468f-9352-01291928cca0" alt="Image 4" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/3dbbb0c0-cb25-4574-8268-98143aadab30" alt="Image 5" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/2ea7f7d7-870a-44af-a337-c97faa521517" alt="Image 3" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/6a9ce83b-2e9d-4d5e-a3cd-03533610dc0d" alt="Image 7" width="200"></td>
  </tr>
</table>

## Project Structure
```
Directory structure:
└── sadiquereyaz-instea/
    ├── README.md
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle.kts
    ├── app/
    │   ├── build.gradle.kts
    │   ├── google-services.json
    │   ├── proguard-rules.pro
    │   ├── .gitignore
    │   └── src/
    │       ├── androidTest/
    │       │   └── java/
    │       │       └── in/
    │       │           └── instea/
    │       │               └── instea/
    │       │                   └── ExampleInstrumentedTest.kt
    │       ├── main/
    │       │   ├── AndroidManifest.xml
    │       │   ├── java/
    │       │   │   └── in/
    │       │   │       └── instea/
    │       │   │           └── instea/
    │       │   │               ├── InsteaApplication.kt
    │       │   │               ├── MainActivity.kt
    │       │   │               ├── composable/
    │       │   │               │   ├── AcademicsComposable.kt
    │       │   │               │   ├── AddAcademicPopup.kt
    │       │   │               │   ├── AttendanceComposable.kt
    │       │   │               │   ├── AuthenticationButton.kt
    │       │   │               │   ├── BottomNavigationBar.kt
    │       │   │               │   ├── BottomSheet.kt
    │       │   │               │   ├── CalendarComposable.kt
    │       │   │               │   ├── DayDateLayout.kt
    │       │   │               │   ├── Dropdown.kt
    │       │   │               │   ├── DropdownButtonComposable.kt
    │       │   │               │   ├── DropdownComposable.kt
    │       │   │               │   ├── ExposedDropDown.kt
    │       │   │               │   ├── InsteaTopAppBar.kt
    │       │   │               │   ├── Loader.kt
    │       │   │               │   ├── MonthYearPicker.kt
    │       │   │               │   ├── PlusMinusBtn.kt
    │       │   │               │   ├── ScheduleItem.kt
    │       │   │               │   ├── ScheduleList.kt
    │       │   │               │   ├── TaskAttendance.kt
    │       │   │               │   └── TaskComposable.kt
    │       │   │               ├── data/
    │       │   │               │   ├── AppContainer.kt
    │       │   │               │   ├── BottomNavItem.kt
    │       │   │               │   ├── FeedViewModel.kt
    │       │   │               │   ├── dao/
    │       │   │               │   │   ├── InsteaDatabase.kt
    │       │   │               │   │   ├── NoticeDao.kt
    │       │   │               │   │   ├── PostDao.kt
    │       │   │               │   │   └── ScheduleDao.kt
    │       │   │               │   ├── datamodel/
    │       │   │               │   │   ├── Message.kt
    │       │   │               │   │   ├── NoticeModal.kt
    │       │   │               │   │   ├── PostData.kt
    │       │   │               │   │   ├── ReminderModal.kt
    │       │   │               │   │   ├── RoomPostModal.kt
    │       │   │               │   │   ├── ScheduleModel.kt
    │       │   │               │   │   ├── StringListResult.kt
    │       │   │               │   │   └── User.kt
    │       │   │               │   ├── repo/
    │       │   │               │   │   ├── AcademicRepository.kt
    │       │   │               │   │   ├── AccountService.kt
    │       │   │               │   │   ├── ChatRepository.kt
    │       │   │               │   │   ├── FeedRepository.kt
    │       │   │               │   │   ├── LocalUserRepository.kt
    │       │   │               │   │   ├── NetworkUserRepository.kt
    │       │   │               │   │   ├── PostRepository.kt
    │       │   │               │   │   ├── ScheduleRepository.kt
    │       │   │               │   │   ├── UserRepository.kt
    │       │   │               │   │   ├── WorkManagerTaskRepository.kt
    │       │   │               │   │   └── notice/
    │       │   │               │   │       ├── LocalNoticeRepository.kt
    │       │   │               │   │       ├── NetworkNoticeRepository.kt
    │       │   │               │   │       ├── NoticeRepository.kt
    │       │   │               │   │       └── WebSrapingService.kt
    │       │   │               │   └── viewmodel/
    │       │   │               │       ├── AppViewModelProvider.kt
    │       │   │               │       ├── AuthViewModel.kt
    │       │   │               │       ├── AuthenticationViewModel.kt
    │       │   │               │       ├── ChatviewModel.kt
    │       │   │               │       ├── EditScheduleViewModel.kt
    │       │   │               │       ├── FeedViewModel.kt
    │       │   │               │       ├── MoreViewModel.kt
    │       │   │               │       ├── NetworkUtils.kt
    │       │   │               │       ├── NoticeViewModel.kt
    │       │   │               │       ├── OtherProfileViewModel.kt
    │       │   │               │       ├── ProfileViewModel.kt
    │       │   │               │       ├── ScheduleViewModel.kt
    │       │   │               │       └── UserInfoViewModel.kt
    │       │   │               ├── navigation/
    │       │   │               │   ├── InsteaNavHost.kt
    │       │   │               │   └── ScreenRoutesConstant.kt
    │       │   │               ├── screens/
    │       │   │               │   ├── AttendanceScreen.kt
    │       │   │               │   ├── DialogBox.kt
    │       │   │               │   ├── InboxScreen.kt
    │       │   │               │   ├── InsteaApp.kt
    │       │   │               │   ├── Feed/
    │       │   │               │   │   ├── CommentCard.kt
    │       │   │               │   │   ├── CommentList.kt
    │       │   │               │   │   ├── EditComment.kt
    │       │   │               │   │   ├── EditPost.kt
    │       │   │               │   │   ├── Feed.kt
    │       │   │               │   │   ├── GetUserData.kt
    │       │   │               │   │   ├── GetpostData.kt
    │       │   │               │   │   ├── InboxScreen.kt
    │       │   │               │   │   ├── PostCard.kt
    │       │   │               │   │   ├── PostList.kt
    │       │   │               │   │   ├── ReplyCard.kt
    │       │   │               │   │   ├── ReplyList.kt
    │       │   │               │   │   ├── SearchScreen.kt
    │       │   │               │   │   └── UserListScreen.kt
    │       │   │               │   ├── auth/
    │       │   │               │   │   ├── AuthUiState.kt
    │       │   │               │   │   ├── AuthenticationScreen.kt
    │       │   │               │   │   ├── SignInScreen.kt
    │       │   │               │   │   ├── UserInfoScreen.kt
    │       │   │               │   │   ├── UserInfoUiState.kt
    │       │   │               │   │   └── composable/
    │       │   │               │   │       ├── ButtonComp.kt
    │       │   │               │   │       ├── CheckboxComp.kt
    │       │   │               │   │       ├── CustomTextField.kt
    │       │   │               │   │       ├── DividerTextComp.kt
    │       │   │               │   │       ├── HeadingText.kt
    │       │   │               │   │       ├── PasswordTextField.kt
    │       │   │               │   │       ├── PasswordVisibilityToggleIcon.kt
    │       │   │               │   │       ├── ScreenChangeText.kt
    │       │   │               │   │       ├── UnderlinedTextComp.kt
    │       │   │               │   │       ├── WelcomeText.kt
    │       │   │               │   │       └── signupSignin.kt
    │       │   │               │   ├── more/
    │       │   │               │   │   ├── MoreScreen.kt
    │       │   │               │   │   ├── MoreUiState.kt
    │       │   │               │   │   └── composable/
    │       │   │               │   │       ├── AccountComp.kt
    │       │   │               │   │       ├── AllTask.kt
    │       │   │               │   │       ├── AttendanceComp.kt
    │       │   │               │   │       ├── Classmates.kt
    │       │   │               │   │       ├── Developers.kt
    │       │   │               │   │       └── report.kt
    │       │   │               │   ├── notice/
    │       │   │               │   │   ├── NoticeScreen.kt
    │       │   │               │   │   └── NoticeUiState.kt
    │       │   │               │   ├── profile/
    │       │   │               │   │   ├── ProfileScreen.kt
    │       │   │               │   │   └── ProfileUiState.kt
    │       │   │               │   └── schedule/
    │       │   │               │       ├── AddSubjectPopup.kt
    │       │   │               │       ├── EditScheduleScreen.kt
    │       │   │               │       ├── EditScheduleUiState.kt
    │       │   │               │       ├── ScheduleScreen.kt
    │       │   │               │       ├── ScheduleUiState.kt
    │       │   │               │       └── TimePicker.kt
    │       │   │               ├── ui/
    │       │   │               │   └── theme/
    │       │   │               │       ├── Color.kt
    │       │   │               │       ├── Theme.kt
    │       │   │               │       └── Type.kt
    │       │   │               ├── utility/
    │       │   │               │   ├── NetworkUtils.kt
    │       │   │               │   ├── NotificationConstant.kt
    │       │   │               │   ├── NotificationPermissionUtils.kt
    │       │   │               │   ├── UserInfoUtil.kt
    │       │   │               │   └── Validator.kt
    │       │   │               └── worker/
    │       │   │                   ├── Constants.kt
    │       │   │                   ├── DailyClassReminderWorker.kt
    │       │   │                   ├── TaskReminderWorker.kt
    │       │   │                   └── WorkerUtils.kt
    │       │   └── res/
    │       │       ├── drawable/
    │       │       │   ├── custom_ripple.xml
    │       │       │   ├── dp.JPG
    │       │       │   ├── google.xml
    │       │       │   ├── ic_launcher_foreground.xml
    │       │       │   ├── insta.xml
    │       │       │   ├── linked.xml
    │       │       │   └── more.xml
    │       │       ├── drawable-nodpi/
    │       │       ├── raw/
    │       │       │   ├── hand_loader.json
    │       │       │   ├── paperplane_loader.json
    │       │       │   └── paperplane_loader2.json
    │       │       ├── values/
    │       │       │   ├── colors.xml
    │       │       │   ├── ic_launcher_background.xml
    │       │       │   ├── strings.xml
    │       │       │   └── themes.xml
    │       │       └── xml/
    │       │           ├── backup_rules.xml
    │       │           └── data_extraction_rules.xml
    │       └── test/
    │           └── java/
    │               └── in/
    │                   └── instea/
    │                       └── instea/
    │                           └── ExampleUnitTest.kt
    └── gradle/
        ├── libs.versions.toml
        └── wrapper/
            └── gradle-wrapper.properties

```

