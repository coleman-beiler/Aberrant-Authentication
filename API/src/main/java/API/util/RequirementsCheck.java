package API.util;

public class RequirementsCheck {
    /*
       Requirements:
       Password Longer Than 8 Characters.
       Atleast One Capital Letter.
       Atleast One Lowercase Letter.
       Atleast One Number.
       Atleast One Special Character.
     */
    public Boolean verifyPasswordMeetsRequirement(String password){
        char character;
        boolean captialFlag = false;
        boolean lowerFlag = false;
        boolean numberFlag = false;
        boolean specialFlag = false;
        boolean lengthFlag = (password.length() >= 8);
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
        for(int i=0; i < password.length(); i++){
            character = password.charAt(i);
            if(Character.isDigit(character)){
                numberFlag = true;
            } else if(Character.isUpperCase(character)){
                captialFlag = true;
            } else if(Character.isLowerCase(character)){
                lowerFlag = true;
            } else if(specialChars.indexOf(character) != -1){
                specialFlag = true;
            }
        }

        if(captialFlag && lowerFlag && numberFlag && specialFlag && lengthFlag){
            return true;
        } else {
            return false;
        }

    }
}
