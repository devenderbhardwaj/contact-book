import { ContactService } from "../Model/ContactService.js";
import Label from "../Model/Label.js";
import LabelService from "../Model/LabelsService.js";
import { CreateLabelDialog } from "./CreateLabelDialog.js";

export class SideBarView {
    #element;
    #createContactBtn;
    #labelList;
    #createContact;
    #labelListHeader;
    #checked = new Set();

    constructor() {
        this.#element = document.createElement("section");
        this.#element.className = "sidebar";

        const padder = document.createElement("div");
        padder.className = "padder";

        this.#createContactBtn = this.#getCreateContactButton();
        this.#labelList = document.createElement("ul");
        this.#labelListHeader = this.#getLabelListHeader();

        padder.appendChild(this.#createContactBtn);
        padder.appendChild(this.#labelList);
        padder.appendChild(this.#labelListHeader);

        this.#element.appendChild(padder);
        this.#element.appendChild(this.#getCloseBtn());
        this.#labelList.addEventListener("input", (e) => {
            const target = e.target;
            const target_id = target.dataset["id"];
            if (target.checked) {
                this.#checked.add(target_id);
            } else {
                this.#checked.delete(target_id);
            }
            ContactService.filterLabel(this.#checked);
        })
    }

    getViewElement() {
        return this.#element;
    }

    /**
    * 
    * @param {[Label]} labels 
    * @returns 
    */
    update(labels) {
        const fragement = document.createDocumentFragment();
        labels.forEach(label => fragement.append(this.#getListItem(label)));
        this.#labelList.innerHTML = "";
        this.#labelList.appendChild(fragement);
    }

    bindOnContactCreate(callBack) {
        this.#createContact = callBack;
    }

    #getLabelListHeader() {
        const element = document.createElement("div");

        const button = document.createElement("button");
        button.className = "add-label";
        button.type = "button";
        button.textContent = "Add label";
        button.addEventListener("click", () => new CreateLabelDialog(this.#onCreatelabel).show());

        element.append(button);
        return element;
    }
    #getCreateContactButton() {
        const button = document.createElement("button");
        button.className = "create-contact";
        button.type = "button";
        button.textContent = "Create Contact";
        button.addEventListener("click", () => {
            this.#createContact();
            this.#close();
        });
        return button;
    }

    #getCloseBtn() {
        const button = Object.assign(document.createElement("button"), {
            type: "button",
            className: "close-btn",
            textContent: "Close"
        })
        button.addEventListener("click", () => {
            console.log(this.#element);
            this.#close();
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
        return item;
    }

    #onCreatelabel = (text) => {
        LabelService.addLabel(text);
    }
    #close = () => {
        document.body.classList.remove("fixed-sidebar-show");
    }
}



