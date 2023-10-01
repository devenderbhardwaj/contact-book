import { Contact } from "../Model/Contact.js";
import { ContactService } from "../Model/ContactService.js";
import { ContactView } from "./ContactView.js";
import { ContactsTableView } from "./ContactsTableView.js";
import { getCreateContactForm } from "./CreateContactForm.js";
import { DeleteDialog } from "./deleteDialog.js";

export class MainContentArea {
    #element;
    #contactsTableView;

    constructor() {
        this.#element = document.createElement("section");
        this.#element.className = "main-content-area";
        this.#contactsTableView = new ContactsTableView();
        this.#element.appendChild(this.#contactsTableView.getViewElement());

        this.#contactsTableView.bindOnEdit(this.#onContactEditModeEnable);
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
     * 
     * @param {Contact} contacts 
     */
    #onContactsLoad = (contacts) => {
        this.#contactsTableView.update(contacts)
    }

    /**
     * 
     * @param {Contact} contact 
     */
    #onContactEditModeEnable = (contact) => {
        this.#element.innerHTML = "";
        const contactView = this.#getContactView(contact, true);
        this.#element.appendChild(contactView.getViewElement());
    }

    /**
     * @param {Contact}
     */
    #onContactView = (contact) => {
        this.#element.innerHTML = "";
        const contactView = this.#getContactView(contact, false);
        this.#element.appendChild(contactView.getViewElement());
    }

    search = (term) => {
        this.#contactsTableView.update(ContactService.filterSearch(term));
    }

    /**
     * 
     * @param {Contact} contact 
     * @param {boolean} editMode 
     * @returns {ContactView}
     */
    #getContactView = (contact, editMode = false) => {
        const contactView = new ContactView(contact, editMode);
        const onEditEnable = () => {
            this.#element.innerHTML = "";
            const contactView = this.#getContactView(contact, true);
            contactView.bindEditEnable(this.#onContactEditModeEnable);
            this.#element.appendChild(contactView.getViewElement());
        }
        const onEditCancel = () => {
            this.#element.innerHTML = "";
            const contactView = this.#getContactView(contact, false);
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
            const onYes = () => ContactService.deleteContact(contact.id, {successCallBack});
            const deleteDialog = new DeleteDialog(onYes);
            deleteDialog.show();
        }
        const onGoBack = () => {
            this.#element.innerHTML = "";
            this.#element.append(this.#contactsTableView.getViewElement());
        }
        const onReload = this.#onContactView;
        
        contactView.bindDelete(onDelete);
        contactView.bindEditCancel(onEditCancel);
        contactView.bindEditEnable(onEditEnable);
        contactView.bindEditSave(onEditSave);
        contactView.bindGoBack(onGoBack);
        contactView.bindOnReload(onReload);
        return contactView;
    }
}