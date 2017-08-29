# EasyGallery
Android gallery query tools to making creating a custom gallery easier.
The idea behind this library is to allow developers to create their own UI for a gallery 
without having to figure out how to effectively read the device media and query it quickly. 

## Download
Gradle:

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'za.co.bwmuller:easygallery-core:0.0.1'
}
```

## How do I Use?
#### Permission
The library requires two permissions:
- `android.permission.READ_EXTERNAL_STORAGE`
- `android.permission.WRITE_EXTERNAL_STORAGE`
- `android.permission.INTERNET`

So if you are targeting Android 6.0+, you need to handle runtime permission request before next step.

### Implementation
Please look at the ui in the easyGallery. It contains a simple implementation of the core reader
The easy permissions module can also be used. It contains some simple code that makes requesting permissions from
anywhere simpler.

## Thanks
This library is inspired by [Matisse](https://github.com/zhihu/Matisse) and uses some of its source code.

## License

    Copyright 2017 Bernhard Müller.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
