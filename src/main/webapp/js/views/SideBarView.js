import { ContactService } from "../Model/ContactService.js";
import Label from "../Model/Label.js";
import LabelService from "../Model/LabelsService.js";
import { CreateLabelDialog } from "./CreateLabelDialog.js";

export class SideBarView {
    #element;
        
    //Logical Data
    #checked = new Set(); // Contains Ids of all checked labels
    
    constructor() {
        this.#element = document.createElement("section");
        this.#element.className = "sidebar";

        const padder = document.createElement("div");
        padder.className = "padder";

        const createContactBtn = this.#getCreateContactButton();
        const labelList = document.createElement("ul");
        const labelListHeader = this.#getLabelListHeader();

        padder.appendChild(createContactBtn);
        padder.appendChild(labelList);
        padder.appendChild(labelListHeader);

        this.#element.appendChild(padder);
        this.#element.appendChild(this.#getCloseBtn());
        labelList.addEventListener("input", (e) => {
            const target = e.target;
            const target_id = target.dataset["id"];
            if (target.checked) {
                this.#checked.add(target_id);
            } else {
                this.#checked.delete(target_id);
            }
            ContactService.filterLabel(this.#checked);
        })

        LabelService.bindOnLoad((labels) => this.#update(labels));
        LabelService.refresh();
    }
    //Event Handlers : Provided by outside
    #onCreateContact;
    
    //Event Handlers binders
    bindOnContactCreate(callBack) {
        this.#onCreateContact = callBack;
    }
    
    //Event Handlers : handles it self
    #onCreatelabel = (text) => {
        LabelService.addLabel(text);
    }
    
    getViewElement() {
        return this.#element;
    }

    /**
    * 
    * @param {Label[]} labels 
    * @returns 
    */
    #update(labels) {
        const fragement = document.createDocumentFragment();
        labels.forEach(label => fragement.appendChild(this.#getListItem(label)));

        const labelList = this.#element.querySelector("ul");
        labelList.innerHTML = "";
        labelList.appendChild(fragement);
    }


    #getLabelListHeader() {
        const element = document.createElement("div");

        const button = document.createElement("button");
        button.className = "add-label";
        button.type = "button";
        button.textContent = "Add label";
        button.addEventListener("click", () => new CreateLabelDialog(this.#onCreatelabel).show());
        element.appendChild(button);
        return element;
    }
    #getCreateContactButton() {
        const button = document.createElement("button");
        button.className = "create-contact";
        button.type = "button";
        button.textContent = "Create Contact";
        button.addEventListener("click", () => {
            this.#onCreateContact();
            this.#close();
        });
        return button;
    }

    #getCloseBtn() {
        const button = Object.assign(document.createElement("button"), {
            type: "button",
            className: "close-btn",
            textContent: "Close",
            onclick: () => {
                this.#close();
            }
        })

        return button;
    }
    /**
     * 
     * @param {Label} label 
     */
    #getListItem(label) {
        const item = document.createElement("li");
        const labelElement = document.createElement("label");
        const checkbox = document.createElement("input");
        checkbox.type = 'checkbox';
        checkbox.dataset["id"] = label.id;
        labelElement.append(checkbox);
        labelElement.append(label.text);
        item.appendChild(labelElement);

        item.addEventListener("dblclick", () => {
            const successCallback = () => { ContactService.refreshLabels() };
            const failureCallback = () => alert("Delete Operation Failed");
            LabelService.deleteLabels(label.id, {successCallback, failureCallback});
        })
        return item;
    }

    #close = () => {
        document.body.classList.remove("fixed-sidebar-show");
    }
}



