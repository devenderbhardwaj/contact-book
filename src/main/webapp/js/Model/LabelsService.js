import Label from "./Label.js";

class LabelServiceClass {
    #labels = [Label];
    #onChangeCallBack;

    constructor() {
        this.#loadLabels();
    }
    
    #loadLabels() {
        const request = new XMLHttpRequest();
        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                console.log(response);
                this.#labels = response.map(label => new Label(label));
                this.refresh();
            }
        }
        request.open("GET", "getLabels");
        request.send();
    }
    bindOnLoad(callBack) {
        this.#onChangeCallBack = callBack;
    }

    refresh() {
        this.#onChangeCallBack?.(this.#labels);
    }

    addLabel(text, {successCallback, failureCallback} = {}) {
        const request = new XMLHttpRequest();
        request.open("POST", "addLabel");
        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                console.log(request.responseText);
                console.log(response);
                if (response.success) {

                    response.label && this.#labels.push(new Label(response.label));
                    this.refresh()
                    successCallback?.();
                } else {
					failureCallback?.();
				}
            }
            console.log(request.status);
            console.log(request.responseText);
        }
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        request.send(`text=${encodeURIComponent(text)}`);
    }

    /**
     * @param {[Number]} ids 
     * @returns 
     */
    getLabels(ids) {
        if (ids == undefined) {
            return this.#labels;
        }
        /**
         * 
         * @param {Label} label 
         */
        const isIt = (label) => {
            return ids.findIndex(id => id == label.id) > -1;
        }
        return this.#labels.filter(isIt);
    }

    /**
     * Add new contact
     * @param {number} label_id - id of label to delete
     * @param {{successCallBack:Function, errorCallBack:Function}} callBacks - callbacks specify action to take after response from server side
     */
    deleteLabels(label_id, {successCallback, failureCallback} = {}) {
        if (this.#labels.find(label => label.id == label_id)) {
            const request = new XMLHttpRequest();
            request.onload = () => {
                console.log(request.response);
                const response = JSON.parse(request.response);
                console.log(response);
                if (response.success) {
                    this.#labels = this.#labels.filter(label => label.id != label_id);
                    console.log(this.#labels);
                    this.refresh();
                    successCallback?.(this.#labels);
                } else {
                    failureCallback?.();
                }
            }
            request.open("POST", 'deleteLabel');
            request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
            const params = new URLSearchParams();
            params.append("label_id", label_id);
            request.send(params);
        } else {
            failureCallback?.();
        }
    }
}

const LabelService = new LabelServiceClass();

export default LabelService;