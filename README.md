# WPExtension
Provides a user-friendly form to provide inputs for custom scripts called within the editor

## Installation
1. Using some program like winrar or 7zip (run as admin), open WPCore.jar, which is located in the installDirectory\lib
2. Put the class file in the org folder
3. Run scripts

---

## Scripting
Within the javascript script file, you need to invoke the org.ScriptExtension.form(String[]) method.
### Parameter
The String array input consists of a bunch of strings with the specific format "name of field, default value, [tooltip], [[min]//max[step]]" (The part in square brackets are optional)

if the name of field is displayName, the default value will become the name of the input window.

default values can be:

* an integer
* a float
* a String (if name of field is label, the string won't be editable, and it will assume there is no tooltip)
* an enum (in this format "[0]ENUM:entry1//entry2//entry3//entry4") where the number before ENUM is the selected index (default is 0)
* a boolean (checkbox for either "true" or "false")

tooltip is the info displayed when the cursor is on top.

min//max//step are for number values. ach value can be null (blank) (when step is null or <=0, it will be 1 for int and 0.1 for float). default is null//null//null. can be inputted as max, min and max, or min and max and step

### Return
The user-input values will be returned in a String array where each String is formatted as "name of field==value returned"
