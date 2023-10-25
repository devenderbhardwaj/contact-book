import { Contact } from "./Contact.js";
import LabelService from "./LabelsService.js";

export class ContactServiceClass {
    /**
     * @property
     * @type {Contact[]}
     */
    contacts = [];
    #filterTerm;
    #filterSet;
    #onContactsLoad = new Map();

    constructor() {
        this.#loadContacts();
    }

    /**
     * Bind callback function which will be called everytime there is change to array of all Contacts
     * @param {string} callbackName - Name of this callback
     * @param {Function} callBack - Function to call when there is some change in contacts
     */
    bindOnContactsLoad(callbackName, callBack) {
        this.#onContactsLoad.set(callbackName, callBack);
    }
    /**
     * 
     * @param {string} callbackName - Callback to be removed
     */
    unBindOnContactLoad(callbackName) {
        this.#onContactsLoad.delete(callbackName);
    }

    #loadContacts() {
        const request = new XMLHttpRequest();
        request.open("POST", "/contacts/getContacts");
        request.send();
        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                if (response.success) {
                    this.contacts = response.data.map(item => new Contact(item));
                    console.log(this.contacts);
                    this.#loaded();
                }
            } else {
                alert("Contact couldn't be fetched");
            }
        }
    }
    #loaded () {
        const arr = this.contacts;
        arr.sort((a, b) => a.name.localeCompare(b.name));
        Array.from(this.#onContactsLoad.values())
            .forEach(callback => callback(arr));
    }
    refreshLabels() {
        this.contacts.forEach(
            contact =>
                contact.labels = LabelService.getLabels(contact.labels.map(label => label.id))
        );
        this.#display();
    }
    /**
     * Display contacts which are passed as array of Contacts if argument is not passed display all contacts
     * @param {[Contact]} arr - array of Contact to display
     */
    #display() {
        let arr = this.contacts;
        arr.sort((a, b) => a.name.localeCompare(b.name));
        arr = arr.filter(item => item.includesTerm(this.#filterTerm));
        arr = this.#filterByLabel(arr, this.#filterSet);
        this.#onContactsLoad.get("displayContacts")?.(arr);
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
                if (response.success) {
                    const contact = new Contact(response.data);
                    this.contacts.push(contact);
                    successCallBack(contact);
                } else {
                    errorCallBack?.();
                }
            } else {
                errorCallBack();
            }
            this.#loaded();
        };
        req.open("POST", "/contacts/saveContact");
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
        req.open("POST", "/contacts/editContact");
        req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        req.onload = () => {
            if (req.status == 200) {
                const response = JSON.parse(req.responseText);
                if (response.success) {
                    const contact = new Contact(response.data);
                    const index = this.contacts.findIndex(item => item.id == contact.id);
                    this.contacts.splice(index, 1, contact);
                    successCallBack?.(contact.id);
                } else {
                    errorCallBack?.();
                }
            } else {
                errorCallBack?.();
            }
            this.#loaded();

        };
        const encodedFormData = new URLSearchParams(formdata).toString();
        req.send(encodedFormData);
    }

    /**
     * 
     * @param {Number} id - id of contact to delete
     * @param {{successCallBack:Function, failureCallback:Function}} callBacks - callbacks specify action to take after response from server side
     */
    deleteContact(id, { successCallBack, failureCallback }) {
        const request = new XMLHttpRequest();
        request.open("POST", "/contacts/delete");
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                if (response.success) {
                    this.contacts = this.contacts.filter(contact => contact.id != id);
                    this.#loaded();
                    successCallBack?.();
                } else {
                    failureCallback?.();
                }
            } else {
                failureCallback?.()
            }
        }
        request.send(`contact_id=${encodeURIComponent(id)}`);
    }

    /**
     * For filter contacts based on text
     * @param {string} term - term to search

     */
    setFilterTerm(term) {
        this.#filterTerm = term;
        this.#display();
    }

    setFilterSet(set) {
        this.#filterSet  = set;
        this.#display();
    }

    /**
     * Display contacts which conform to applied contact label filters
     * @param {Contact[]} arr - arr of contacts from which have to search
     * @param {number[]} ids - array of labels-ids
     * @returns {Set<Contact>}
     */
    #filterByLabel(arr, ids) {
        const set = new Set(arr);
        ids.forEach(id => {
            this.contacts.forEach(contact => {
                const result = contact.hasLabel(id);
                if (!result) set.delete(contact);
            });
        })

        return set
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
                if (resposne.success) {
                    const index = this.contacts.findIndex(c => c.id == contact.id);
                    this.contacts[index].labels = resposne.data.labels;
                    successCallBack?.(this.contacts[index]);
                    this.#loaded();
                }
                else {
                    failureCallback?.();
                }
            }
            else {
                failureCallback?.();
                alert(request.statusText);
            }
        }
        request.open("POST", "/contacts/editLabels")
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        const params = new URLSearchParams();
        params.append("contact_id", contact.id);
        checked.forEach(id => params.append("id", id));
        request.send(params);
    }

    /**
     * 
     * @param {number} contact_id 
     * @returns {Contact}
     */
    getContactById(contact_id) {
        return this.contacts.find(c => c.id == contact_id);
    }
}


export const ContactService = new ContactServiceClass();