MyResumeIsOnGithub
==================

MyResumeIsOnGithub is designed to provide a framework for providing your resume as a Android Application (APK) with only minor configuration and providing your resume information in the required JSON format.


Instructions
============


This application requires Android Studio 0.2.2+ and Gradle 0.5+. Please ensure you have those installed and configured before beginning.

1. Check out the source code
2. In Android Studio, select Import Project
3. Select the root directory you checked out
4. Select _Import project from external model_ and choose Gradle
5. Check _Use auto-import_ and ensure that _Use gradle wrapper (recommended)_ is selected

This should generate the appropriate Android Studio files and allow you the build the sample application. To add your own resume details, you must add a new [Product flavor][1], either in addition to or replacing the *sample* product flavor.

To add a new product flavor:
1. Edit _MyResumeIsOnGithub/build.gradle_, adding your new product flavor to the _productFlavors_ section, giving it a unique package name.
2. In the _MyResumeIsOnGithub/src_ directory, copy the _sample_ directory to your new product flavor's name. This will ensure you have all of the files required to build a product flavor successfully.
3. Edit each file in your new _src_ directory, replacing Andy Royd's information with your own.


License
=======

    Copyright 2013 Ian Lake & Josh Brown

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Product-flavors