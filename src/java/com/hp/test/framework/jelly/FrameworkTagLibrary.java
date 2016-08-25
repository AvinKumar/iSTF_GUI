package com.hp.test.framework.jelly;

import com.hp.test.framework.DSSpecific.*;
import org.apache.commons.jelly.TagLibrary;
//import com.hp.test.framework.eca.*;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class FrameworkTagLibrary extends TagLibrary {

    public FrameworkTagLibrary() {
        this.registerTag("startbrowser", StartBrowserTag.class);
        this.registerTag("stopbrowser", StopBrowserTag.class);
        this.registerTag("christen", ChristenTag.class);
        this.registerTag("assert", Assert.class);
        this.registerTag("action", Action.class);
        this.registerTag("execute", Execute.class);
        this.registerTag("sleep", SleepTag.class);
        this.registerTag("deletedir", DeleteDirectoryTag.class);
        this.registerTag("propertyresourcebundle", PropertyResourceBundleTag.class);
        this.registerTag("login", LoginTag.class);
        this.registerTag("type", TypeTag.class);
        this.registerTag("presskey", PressKeyTag.class);
        this.registerTag("click", ClickTag.class);
        this.registerTag("select", SelectTag.class);
        this.registerTag("table", TableTag.class);
        this.registerTag("uploadfile", UploadFileTag.class);
        this.registerTag("verifyelementstate", VerifyEnableDisableTag.class);
        this.registerTag("check", SelectCheckBoxTag.class);
        this.registerTag("clicklink", ClickLinkTag.class);
        this.registerTag("verifytext", VerifyTextTag.class);
        this.registerTag("verifyelementexist", VerifyElementOnPageTag.class);
        this.registerTag("verifyelementnotexist", VerifyElementNotExistTag.class);
        this.registerTag("verifyenable", VerifyEnableTag.class);
        this.registerTag("verifydisable", VerifyDisableTag.class);
        this.registerTag("verifydisplayed", VerifyDisplayedTag.class);
        this.registerTag("verifynotdisplayed", VerifyNotDisplayedTag.class);
        this.registerTag("verifytitleofpage", VerifyTitleOfPage.class);
        this.registerTag("switchtoframe", SwitchToFrameTag.class);
        this.registerTag("switchtoiframe", SwitchToiFrameTag.class);
        this.registerTag("clicklinkintable", ClickLinkinTableTag.class);
        this.registerTag("clickcheckboxbasedontextlinkintable", ClickCheckboxBasedonTextLinkinTableTag.class);
        this.registerTag("clickradiobasedontextlinkintable", ClickRadioBasedonTextLinkinTableTag.class);
        this.registerTag("implicitwait", ImplicitWaitTag.class);
        this.registerTag("refreshbrowser", RefreshBrowserTag.class);
        this.registerTag("presskey", PressKeyTag.class);
        this.registerTag("rightclick", RightClickTag.class);
        this.registerTag("mousehover", MouseHoverTag.class);
        this.registerTag("doubleclick", DoubleClickTag.class);
        this.registerTag("acceptpopup", AcceptPopupTag.class);
        this.registerTag("cancelpopup", CancelPopupTag.class);
        this.registerTag("deletecookies", DeleteCookiesTag.class);
        this.registerTag("deletedirectory", DeleteDirectoryTag.class);
        this.registerTag("autoitclickokonprint", AutoitClickOKonPrintTag.class);
        this.registerTag("switchtochildbrowser", SwitchToChildBrowserTag.class);
        this.registerTag("switchtoparentbrowser", SwitchToParentBrowserTag.class);
        this.registerTag("presskeyonbrowser", PressKeyonBrowserTag.class);
        this.registerTag("waituntilelementclickable", WaitUntilElementClickableTag.class);
        this.registerTag("waituntilelementvisible", WaitUntilElementVisibleTag.class);
        this.registerTag("verifytextnotexist", VerifyTextNotExistTag.class);
        this.registerTag("clickOkonwindialog", ClickOKonWinDialogTag.class);
        this.registerTag("selectsaveandokondownloaddialog", SelectSaveandOKonDownloadDialogTag.class);
        this.registerTag("getvariable", GetVariabletag.class);
        this.registerTag("popupverifytext", PopupVerifyTextTag.class);
        this.registerTag("userfunctioncall", UserFunctionCallTag.class);
        this.registerTag("ftpfromlinuxtowindows", ftpfromLinuxtoWindowsTag.class);
        this.registerTag("verifyurl", VerifyUrlTag.class);
        //this.registerTag("draganddrop", DragAndDrop.class);
        this.registerTag("cleartext",ClearTextTag.class);
        
    }
}
