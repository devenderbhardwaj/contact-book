import { Contact } from "../Model/Contact.js";
import { ContactService } from "../Model/ContactService.js";
import { ContactView } from "./ContactView.js";
import { ContactsTableView } from "./ContactsTableView.js";
import { getCreateContactForm } from "./CreateContactForm.js";
import { DeleteDialog } from "./deleteDialog.js";

class MainContentAreaClass {
    #element;
    #contactsTableView;

    constructor() {
        this.#element = document.createElement("section");
        this.#element.className = "main-content-area";
        this.#contactsTableView = new ContactsTableView();
        this.#element.appendChild(this.#contactsTableView.getViewElement());

        this.#contactsTableView.bindOnView(this.#onContactView);
        this.#contactsTableView
    }

    getViewElement() {
        return this.#element;
    }
    createContact() {
        this.#element.innerHTML = "";
        const form = getCreateContactForm(() => {
            this.#element.innerHTML = "";
            this.#element.append(this.#contactsTableView.getViewElement());
        }, ContactService);
        this.#element.append(form);
    }

    /**
     * @param {Contact} contact
     * @param {boolean} editMode
     */
    #onContactView = (contact, editMode = false) => {
        this.#element.innerHTML = "";
        const contactView = this.#getContactView(contact, editMode);
        this.#element.appendChild(contactView.getViewElement());
    }

    /**
     * 
     * @param {Contact} contact 
     * @param {boolean} editMode 
     * @returns {ContactView}
     */
    #getContactView = (contact, editMode = false) => {
        const contactView = new ContactView(contact, editMode);
        const reload = (editMode = false) => {
            this.#element.innerHTML = "";
            const contactView = this.#getContactView(contact, editMode);
            this.#element.appendChild(contactView.getViewElement());
        }
        /**
         * 
         * @param {FormData} formdata
         */
        const onEditSave = (formdata) => {
            const successCallBack = this.#onContactView;
            const errorCallBack = () => alert("Failed");
            ContactService.editContact(formdata, {successCallBack, errorCallBack})
        }
        const onDelete = () => {
            const successCallBack = onGoBack;
            const failureCallback = () => alert("Delete operation failed");
            const onYes = () => ContactService.deleteContact(contact.id, {successCallBack, failureCallback});
            const deleteDialog = new DeleteDialog(onYes);
            deleteDialog.show();
        }
        const onGoBack = () => {
            this.#element.innerHTML = "";
            this.#element.append(this.#contactsTableView.getViewElement());
        }
        
        contactView.bindDelete(onDelete);
        contactView.bindEditSave(onEditSave);
        contactView.bindGoBack(onGoBack);
        contactView.bindOnReload(reload);
        return contactView;
    }
}

export const MainContentArea = new MainContentAreaClass();