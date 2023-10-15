import { LRUCache } from "../utilities/Cache.js";

class ProfilePictureServiceClass {
    cache = new LRUCache(20);

    /**
     * @param {number} contact_id 
     * @param {String} file
     * @param {{successCallback:Function, errorCallback:Function}} callBacks
     */
    saveProfile(contact_id, file, { successCallback, failureCallback } = {}) {
        if (!file) {
            return;
        }
        const formdata = new FormData();
        formdata.append("contact_id", contact_id);
        formdata.append("image", file);
        const request = new XMLHttpRequest();
        request.open("POST", "saveProfilePicture");
        request.responseType = "json";
        request.send(formdata);
        request.onload = () => {
            if (request.status == 200 && request.response.success) {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    this.cache.set(contact_id, reader.result);
                    successCallback?.(reader.result);
                }
            } else {
                failureCallback?.();
            }
        }
    }

    getProfile(contact_id, { successCallBack, failureCallBack }) {
        if (this.cache.has(contact_id)) {
            successCallBack?.(this.cache.get(contact_id));
            return ;
        }
        const request = new XMLHttpRequest();
        const searchParams = new URLSearchParams();
        searchParams.append("contact_id", contact_id);
        request.open("POST", `getProfilePicture?contact_id=${contact_id}`);
        request.responseType = "blob";
        request.send();
        request.onload = () => {
            if (request.status == 200) {
                const response = request.response;
                const reader = new FileReader();
                reader.readAsDataURL(response);
                reader.onload = () => {
                    this.cache.set(contact_id, reader.result);
                    successCallBack?.(reader.result);
                }
            } else {
                failureCallBack?.();
            }
        }
    }

}

export const ProfilePictureService = new ProfilePictureServiceClass();