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
}

const LabelService = new LabelServiceClass();

export default LabelService;