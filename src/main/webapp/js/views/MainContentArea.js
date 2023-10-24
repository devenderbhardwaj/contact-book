import { Contact } from "../Model/Contact.js";
import { ContactService } from "../Model/ContactService.js";
import { Router } from "../utilities/Router.js";
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
    }

    getViewElement() {
        return this.#element;
    }

    showCreateContact() {
        this.#element.innerHTML = "";
        const form = getCreateContactForm();
        this.#element.append(form);
    }

    /**
     * @param {boolean} push 
     */
    showDefaultView = () => {
        document.title = "Contacts";
        this.#element.innerHTML = "";
        if (this.#contactsTableView) {
            this.#element.append(this.#contactsTableView.getViewElement());
        } else {
            this.#contactsTableView = new ContactsTableView();
            this.#element.append(this.#contactsTableView.getViewElement());
        }
    }
    /**
     * @param {{contact_id:number, editMode:boolean}} contact_id
     */
    showContactById = ({ contact_id, editMode }) => {
        /**
         * 
         * @param {Contact} contacts 
         * @returns 
         */
        const showContact = (contacts) => {
            ContactService.unBindOnContactLoad('displayContact');
            const contact = contacts.find(c => c.id == contact_id);
            if (!contact) {
                history.replaceState(null, null, "/contacts/home");
                Router.route();
                return;
            }
            this.#element.innerHTML = "";
            const contactView = new ContactView(contact, editMode);
            this.#element.appendChild(contactView.getViewElement());
        }
        if (ContactService.contacts.length == 0) {
            ContactService.bindOnContactsLoad('displayContact', showContact);
        } else {
            showContact(ContactService.contacts);
        }
    }
}

export const MainContentArea = new MainContentAreaClass();