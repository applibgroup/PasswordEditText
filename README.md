[![Build](https://github.com/applibgroup/PasswordEditText/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/PasswordEditText/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_PasswordEditText&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_PasswordEditText)

# PasswordEditText
Custom TextField to show/hide password with customizable icon.

# Source
This library has been inspired by [anshulagarwal06\\PasswordEditText](https://github.com/anshulagarwal06/PasswordEditText).

## Integration

1. For using PasswordEditText module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
```
 implementation project(path: ':library')
```
2. For using PasswordEditText module in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
 implementation fileTree(dir: 'libs', include: ['*.har'])
```
3. For using PasswordEditText module from a remote repository in separate application, add the below dependencies in entry/build.gradle file.
```
implementation 'dev.applibgroup:passwordedittext:1.0.0'
```


# How to use


```groovy
	    
  <in.anshul.library.PasswordEditText
            ohos:id="$+id:password_edit_text"
            ohos:height="match_content"
            ohos:width="match_parent"
            ohos:hide_drawable="$media:in_anshul_hide_password"
            ohos:hint="Password"
            ohos:horizontal_center="true"
            ohos:padding="10vp"
            ohos:password_visible="false"
            ohos:show_drawable="$media:in_anshul_show_password"
            ohos:text_size="18fp"/>
	    
	    
```
  Instead of drawable you can also use Text
```groovy        
            

 <in.anshul.libray.PasswordEditText
            ...
            ohos:show_as_text="true"
            ohos:show_text="$string:show_text"
            ohos:hide_text="$string:hide_text"/>
 ```
# Customise
 
 * `ohos:show_drawable="$media:show_password"` 
 * `ohos:hide_drawable="$media:hide_password"`
 
Defualt Password Visiblity can be set using attr 
* `ohos:password_visible="true"`

That's it build your project.
