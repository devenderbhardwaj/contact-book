import { Contact } from "../Model/Contact.js";
import { ContactService } from "../Model/ContactService.js";
import getAllLabelsPopUp from "./AllLabelsPopUp.js";

export class ContactView {
    #contact;
    #element;
    #editMode;

    // Action Listener
    #onDelete;
    #onEditSave;
	#onBack;
	#onReload;

    /**
     * 
     * @param {Contact} contact 
     */
    constructor(contact, editMode = false) {
        this.#contact = contact;
        this.#element = this.#getViewContact(editMode);
        this.#editMode = editMode;
    }

    bindDelete(callBack) {
        this.#onDelete = callBack;
    }
    bindEditSave(callBack) {
        this.#onEditSave = callBack;
    }
    bindGoBack(callBack) {
		this.#onBack = callBack;
	}
    bindOnReload(callBack) {
        this.#onReload = callBack
    }
    getViewElement() {
        return this.#element;
    }

    #getViewContact(editMode = false) {
        const element = document.createElement("div");
        element.className = "view-contact";
        element.dataset['contact_id'] = this.#contact.id;
           element.append(this.#getContactHeader(editMode));
        if (editMode) {
            element.append(this.#getEditContactForm());
        } else {
            element.append(this.#getContactInfoCard());
        }
        const closeBtn = document.createElement("button");
        element.append(closeBtn);
        closeBtn.outerHTML = "<button type='button' class='close-btn'>Close</button>";
        element.querySelector(".close-btn").addEventListener("click", () => this.#onBack() );
       
        return element;
    }
    
    #getEditContactForm() {
        const element = document.createElement("div");
        element.className = "edit-contact";
    
        element.innerHTML =
            `<form method="post" name="edit-contact-form" id='edit-contact-form'>
                <label>Name:
                    <input type="text" name="name" value='${this.#contact.name}'>
                </label>
                <label>Phone:
                    <input type="tel" name="phone" value='${this.#contact.phone}'>
                    </label>
                <label>Email:
                    <input type="email" name="email" value='${this.#contact.email}'>
                </label>
                <label>Address:
                    <textarea name="address" id="" cols="30" rows="10">${this.#contact.address}</textarea>
                </label>
                    <input type='text' name='contact-id' value='${this.#contact.id}'>
            </form>
            `;
    
        const editContactForm = element.querySelector("form");
        editContactForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const formdata = new FormData(editContactForm);
            this.#onEditSave(formdata);
        });
    
        return element;
    }
    
    #getContactHeader( editMode = false) {
        const element = document.createElement("div");
        element.className = "heading";
    
        element.innerHTML = (
            `
            <div class="profile-picture">
                <img src="" alt="profile-picture">
            </div>
            <div class="name">
                <h2>${this.#contact.name}</h2>
                <div class=".labels">
                    ${this.#contact.labels?.map(label => `<span class="contact-label">${label.text}</span>`).join(", ")}
                    <button type='button' class='add-label'>Edit Labels</button>
                </div>
                <div class="controls">
                    ${editMode ? 
                        `<button type='submit' class='save-btn' form='edit-contact-form'>Save</button>
                        <button type='button' class='cancel-btn'>Cancel</button` :
                        `<button type='button' class='delete-btn'>Delete</button>
                        <button type='button' class='edit-btn'>Edit</button>`
                    }
                </div>
            </div>
            `
        );
        const editBtn = element.querySelector(".controls .edit-btn");
        editBtn?.addEventListener("click", () => {
            this.#onReload(true);
        });
    
        const cancelBtn = element.querySelector(".controls .cancel-btn");
        cancelBtn?.addEventListener("click", () => {
            this.#onReload(false);
        })
    
        const deleteBtn = element.querySelector(".controls .delete-btn");
        deleteBtn?.addEventListener("click", () => {
            this.#onDelete();
        })

        element.querySelector("button.add-label").addEventListener("click", () => {
            const dialog = getAllLabelsPopUp(this.#contact, this.#onLabelChange);
            element.append(dialog);
            dialog.showModal();
        });
        return element;
    }
    
    #getContactInfoCard() {
        const element = document.createElement("div");
        element.className = 'info';
    
        element.innerHTML = (
            `
            <div class="info card">
                <p>Name: ${this.#contact.name}</p>
                <p>Phone: ${this.#contact.phone}</p>
                <p>Email: ${this.#contact.email}</p>
                <p>Address: ${this.#contact.address}</p>
            </div>
            `
        );
            
        return element;
    }

    /**
     * 
     * @param {number[]} checked 
     */
    #onLabelChange = (checked) => {
        const successCallBack = () => this.#onReload(this.#editMode) ;
        ContactService.labelsEdit(this.#contact, checked, {successCallBack});
    }
}