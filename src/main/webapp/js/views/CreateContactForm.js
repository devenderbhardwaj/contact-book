import { ContactService } from "../Model/ContactService.js";
import { Router } from "../utilities/Router.js";

/**
 * @param {Function} backAction
 * @returns div element containing form to create a new contact
 */
export function getCreateContactForm() {
    const element = document.createElement("div");
    element.className = "create-contact";

    element.innerHTML =
        `
        <h1>Enter Contact Details</h1>
        <form action="saveContact" method="post">
            <p>
                <label for="name-inp-c">Name:</label>
                <input type="text" name="name" id="name-inp-c" required>
            </p>
            <p>
                <label for="phone-inp-c">Phone:</label>
                <input type="tel" name="phone" id="phone-inp-c">
            </p>
            <p>
                <label for="email-inp-c">Email:</label>
                <input type="email" name="email" id="email-inp-c">
            </p>
            <p>
                <label for="address-inp-c">Address:</label>
                <textarea name="address" id="address-inp-c" cols="30" rows="5"></textarea>
            </p>
            <p><button type="submit">Save</button></p>
        </form>`;

    const createContactForm = element.querySelector("form");
    createContactForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const successCallBack = () => {
            alert("Saved");
            history.back();
        }

        const errorCallBack = () => alert("Contact not Saved");
        const formdata = new FormData(createContactForm);

        ContactService.addContact(formdata, { successCallBack, errorCallBack });
    });
    return element;
}