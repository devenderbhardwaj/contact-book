import { Contact } from "../Model/Contact.js";
import { ContactService } from "../Model/ContactService.js";
import { DeleteDialog } from "./deleteDialog.js";

export class ContactsTableView {
    #element;
    #tablebody;
    #documentFragment = document.createDocumentFragment();
    
    constructor() {
        this.#element = document.createElement("div");
        this.#element.className = "all-contacts-area";

        this.#element.appendChild(this.#getSearchBox());
        this.#element.appendChild(this.#getTableParentElement());
        this.#tablebody = this.#element.querySelector(".table-body");
    
        ContactService.bindOnContactsLoad(contacts => this.update(contacts));
        this.update(ContactService.contacts);
    }

    // Action Listener - provided by outside
    #onEdit ;
    #onView;

    // Action Listener - binders

    /**
     * 
     * @param {Function} callBack 
     */
    bindOnView(callBack) {
        this.#onView = callBack;
    }

    /**
     * 
     * @param {Function} callBack 
     */
    bindOnEdit(callBack) {
        this.#onEdit = callBack;
    }
    

    // Action Listener - defined internally

    /**
     * @param {Contact} contact
     */
    #onDelete = (contact) => {
        const onYes = () => {
            const successCallBack = () => {};
            const failureCallback = () => {alert("Delete Operation Failed")};
            ContactService.deleteContact(contact.id, {successCallBack, failureCallback});
        };
        const deleteDialog = new DeleteDialog(onYes);
        deleteDialog.show();      
    };

    /**
     * 
     * @param {Contact[]} contacts 
     */
    update(contacts) {
       contacts.forEach(contact => this.#documentFragment.append(this.#getRowElement(contact)));
       this.#tablebody.innerHTML = '';
       this.#tablebody.append(this.#documentFragment);
    }

    getViewElement() {
        return this.#element;
    }

    #getTableParentElement() {
        const element = document.createElement("div");
        element.className = "all-contacts";
        element.innerHTML = (
        `
        <div class="table-head">
            <div class="table-row heading">
                <div>Name</div>
                <div>Phone</div>
                <div>Email</div>
                <div>Labels</div>
                <div></div>
            </div>
        </div>
        <div class="table-body">

        </div>
        `
        );
        return element;
    }

    /**
     * 
     * @param {Contact} contact 
     * @returns {HTMLDivElement} 
     */
    #getRowElement(contact) {
        const rowElement = document.createElement("div");
        rowElement.className = "table-row";

        rowElement.innerHTML = (
            `
                <div class='name'>${contact.name}</div>
                <div>${contact.phone}</div>
                <div>${contact.email}</div>
                <div>${contact.labels?.map(label => `<span class="contact-label">${label.text}</span>`).join(", ")}</div>
                <div>
                    <button class='edit'>edit</button>
                    <button class='delete'>delete</button>
                </div>
            `
        );
        rowElement.querySelector(".name").addEventListener("click", () => this.#onView(contact, false));
        rowElement.querySelector("button.edit").addEventListener("click", () => this.#onView(contact, true));
        rowElement.querySelector("button.delete").addEventListener("click", () => this.#onDelete(contact));
        
        return rowElement;
    }

    #getSearchBox() {
        const searchField = document.createElement("input");
        Object.assign(searchField, {
            type: "search",
            name: 'filter-search',
            className: "filter",
            placeholder: "Search",
            oninput: () => this.update(ContactService.filterSearch(searchField.value))
        })
        
        const element = document.createElement("div");
        element.className = "search-box";
        element.appendChild(searchField);
        return element;
    }
}