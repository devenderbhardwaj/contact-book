import { Contact } from "./Contact.js";
import LabelService from "./LabelsService.js";

export class ContactServiceClass {
    /**
     * @property
     * @type {Contact[]}
     */
    contacts = [];
    #onContactsLoad;

    constructor() {
        this.#loadContacts();
    }

    /**
     * Bind callback function which will be called everytime there is change to array of all Contacts
     * @param {Function} callBack - Function to call when there is some change in contacts
     */
    bindOnContactsLoad(callBack) {
        this.#onContactsLoad = callBack;
    }

    #loadContacts() {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "getContacts");
        xhr.send();
        xhr.onload = () => {
            const x = xhr.response;
            const response = JSON.parse(x);
            this.contacts = response.map(item => new Contact(item));
            console.log(this.contacts);
            this.#refresh();
        }
    }

    refreshLabels() {
        this.contacts.forEach(
            contact =>
                contact.labels = LabelService.getLabels(contact.labels.map(label => label.id))
        );
        this.#refresh();
    }
    /**
     * Display contacts which are passed as array of Contacts if argument is not passed display all contacts
     * @param {[Contact]} arr - array of Contact to display
     */
    #refresh(arr) {
        arr = arr ?? this.contacts;
        arr.sort((a, b) => a.name.localeCompare(b.name));
        this.#onContactsLoad?.(arr);
    }

    /**
     * Add new contact
     * @param {FormData} formdata - formdata to related to contact
     * @param {{successCallBack:Function, errorCallBack:Function}} callBacks - callbacks specify action to take after response from server side
     */
    addContact(formdata, { successCallBack, errorCallBack }) {
        const req = new XMLHttpRequest();

        req.onload = () => {
            if (req.status === 200) {
                const response = JSON.parse(req.responseText);
                const contact = new Contact(response);
                this.contacts.push(contact);
                successCallBack(contact);
            } else {
                errorCallBack();
            }
            this.#refresh();
        };
        req.open("POST", "saveContact");
        req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        const encodedFormData = new URLSearchParams(formdata).toString();
        req.send(encodedFormData);
    }

    /**
     * @param {FormData} formdata  - formdata to related to contact
     * @param {{successCallBack:Function, errorCallBack:Function}} callBacks - callbacks specify action to take after response from server side
     */
    editContact(formdata, { successCallBack, errorCallBack }) {
        const req = new XMLHttpRequest();
        req.open("POST", "editContact");
        req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        req.onload = () => {
            if (req.status == 200) {
                const responseContact = JSON.parse(req.responseText);
                const contact = new Contact(responseContact);
                const index = this.contacts.findIndex(item => item.id == contact.id);
                this.contacts.splice(index, 1, contact);
                successCallBack?.(contact);
            } else {
                errorCallBack?.();
            }
            this.#refresh();

        };
        const encodedFormData = new URLSearchParams(formdata).toString();
        req.send(encodedFormData);
    }

    /**
     * 
     * @param {Number} id - id of contact to delete
     * @param {{successCallBack:Function, errorCallBack:Function}} callBacks - callbacks specify action to take after response from server side
     */
    deleteContact(id, { successCallBack, errorCallBack }) {
        const request = new XMLHttpRequest();
        request.open("POST", "delete");
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                if (response.deleted) {
                    this.contacts = this.contacts.filter(contact => contact.id != id);
                    this.#refresh();
                    successCallBack?.();
                } else {
                    errorCallBack?.();
                }
            } else {
                errorCallBack?.()
            }
        }
        request.send(`contact_id=${encodeURIComponent(id)}`);
    }

    /**
     * 
     * @param {string} term - term to search
     * @returns {Contact[]} - Array of all Contacts which includes the term
     */
    filterSearch(term) {
        return this.contacts.filter(item => item.includesTerm(term));
    }

    /**
     * Display contacts which conform to applied contact label filters
     * @param {number[]} ids - array of labels-ids 
     */
    filterLabel(ids) {
        const set = new Set(this.contacts);
        ids.forEach(id => {
            this.contacts.forEach(contact => {
                const result = contact.hasLabel(id);
                if (!result) set.delete(contact);
            });
        })

        this.#refresh(Array.from(set));
    }

    /**
     * Edit Labels of contact
     * @param {Contact} contact - Contact to which we are making changed
     * @param {number[]} checked - ids of all labels which will apply to this contact
     * @param {{successCallBack:Function, failureCallback:Function}} - callbacks specify action to take after response from server side
     */
    labelsEdit(contact, checked, { successCallBack, failureCallback }) {
        const request = new XMLHttpRequest();
        request.onload = () => {
            if (request.status == 200) {
                const resposne = JSON.parse(request.responseText);
                if (resposne.contact) {
                    const index = this.contacts.findIndex(c => c.id == contact.id);
                    this.contacts[index].labels = resposne.contact.labels;
                    successCallBack?.(this.contacts[index]);
                    this.#refresh();
                }
                else {
                    failureCallback?.();
                }
            }
            else {
                alert(request.statusText);
            }
        }
        request.open("POST", "editLabels")
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        const params = new URLSearchParams();
        params.append("contact_id", contact.id);
        checked.forEach(id => params.append("id", id));
        request.send(params);
    }
}


export const ContactService = new ContactServiceClass();