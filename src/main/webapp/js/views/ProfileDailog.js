import { ProfilePictureService } from "../Model/ProfilePictureService.js";


export class ProfileDialog {
    
	
    constructor(contact_id, imageSrc, {successCallback, failureCallback} = {}) {
        this.element = document.createElement("dialog");
        this.element.className = "profile";
        this.element.innerHTML = (
            `
            <div>
                <img src"" alt="Profile Picture">
                <div class='controls'>
                    <input type='file' accept='image/*' class='image-input'>
                    <button type='button' class='upload-btn' disabled>Upload</button>
                </div>
            </div>
            <button type='button' class='close-btn'>x</button>
            `
        );
        const imageInput = this.element.querySelector(".image-input");
        const imageDisplay = this.element.querySelector("img");
        const uploadBtn = this.element.querySelector(".upload-btn");
        const closeBtn = this.element.querySelector(".close-btn");
        uploadBtn.addEventListener("click", () => {
            uploadBtn.disabled = true;
            imageInput.disabled = true;
            closeBtn.disabled = true;
            const s = (url) => {
                successCallback?.(url);
                this.element.close();
            }
            const f = () => {
                uploadBtn.disabled = false;
                imageInput.disabled = false;
                closeBtn.disabled = false;
                alert("Could n't change the profile picture");
                failureCallback?.();
            }
            ProfilePictureService.saveProfile(contact_id, imageInput.files[0], {successCallback:s, failureCallback:f});
        })
        this.element.addEventListener("close", () => {
            this.element.remove();
        })
        closeBtn.addEventListener("click", () => {
            this.element.close();
        })
        imageInput.addEventListener("input", () => {
            const reader = new FileReader();
            reader.readAsDataURL(imageInput.files[0]);
            reader.onload = () => {
                uploadBtn.disabled = false;
                imageDisplay.src = reader.result;
            }
        })
        imageDisplay.src = imageSrc
        document.body.append(this.element);
    }

    show() {
        this.element.showModal();
    }

}


