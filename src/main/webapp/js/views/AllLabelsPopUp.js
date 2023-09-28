import { Contact } from "../Model/Contact.js";
import Label from "../Model/Label.js";
import LabelService from "../Model/LabelsService.js";

/**
 * 
 * @param {Contact} contact 
 * @returns 
 */
export default function getAllLabelsPopUp(contact, onYes) {
    const list = document.createElement("ul");
    LabelService.getLabels().forEach(label => list.appendChild(getListItem(contact,label)));

    const form = document.createElement("form");
    form.method = "dialog";
    form.append(list);

    form.appendChild(getControls());

    const element = document.createElement("dialog");
    element.appendChild(form);
    element.className = "all-labels-pop-up";
    element.addEventListener("close", () => {
        const checked = [];
        element.querySelectorAll("input").forEach(
            checkbox => {
                if (checkbox.checked) {
                    checked.push(checkbox.dataset.id);
                }
            }
        )
        if (element.returnValue == 'yes') {
            onYes(checked);
        }
        element.remove();
    });
    return element;

}

/**
 * @param {Contact} contact
 * @param {Label} label 
 */
function getListItem(contact, label) {
    const item = document.createElement("li");
    const labelElement = document.createElement("label");
    const checkbox = document.createElement("input");
    checkbox.type = 'checkbox';
    checkbox.dataset["id"] = label.id;
    if (contact.hasLabel(label.id)) {
        checkbox.checked = true;
    }
    labelElement.append(checkbox);
    labelElement.append(label.text);
    item.appendChild(labelElement);
    return item;
}

function getControls() {
    const element = document.createElement("div");
    element.className = "controls";

    const cancelBtn = document.createElement("button");
    cancelBtn.textContent = "Cancel";
    cancelBtn.type = "submit";
    cancelBtn.value = "no";

    const okBtn = document.createElement("button");
    okBtn.textContent = "Save";
    okBtn.type = "submit";
    okBtn.value = "yes";

    element.appendChild(cancelBtn);
    element.appendChild(okBtn);

    return element;
}