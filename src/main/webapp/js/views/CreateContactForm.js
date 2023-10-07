import { ContactServiceClass } from "../Model/ContactService.js";

/**
 * @param {Function} backAction
 * @param {ContactServiceClass} ContactService 
 * @returns div element containing form to create a new contact
 */
export function getCreateContactForm(backAction, ContactService) {
	const element = document.createElement("div");
    element.className = "create-contact";

    element.innerHTML =
        `<form action="saveContact" method="post">
            <label>Name:
                <input type="text" name="name" required>
            </label>
            <label>Phone:
                <input type="tel" name="phone">
                </label>
            <label>Email:
                <input type="email" name="email">
            </label>
            <label>Address:
                <textarea name="address" id="" cols="30" rows="10"></textarea>
            </label>
            <button type="submit">Save</button>
        </form>

        <button type="button" class="close-btn">Close</button>`;

    const createContactForm = element.querySelector("form");
    createContactForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const successCallBack = () => {
            alert("Saved");
            backAction();
        }

        const errorCallBack = () => alert("Contact not Saved");
        const formdata = new FormData(createContactForm);

        ContactService.addContact(formdata, {successCallBack, errorCallBack});
    });
    element.querySelector(".close-btn").addEventListener("click", () => backAction());
    return element;
}