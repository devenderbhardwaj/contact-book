import Label from "./Label.js";

export class Contact {
    /**
     * @property {[Label]}
     */
    labels;
    constructor({contact_id = 0,name = "",phone = "", email = "",address = "", labels = []} ) {
        this.id = contact_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.labels = labels?.map(label => new Label(label));
    }

    /**
     * 
     * @param {string} term - term to search
     * @returns {boolean} return true if some field Contact includes term false otherwise
     */
    includesTerm(term) {
        term = term?.toLowerCase() ?? ""
        return (
            this.name.toLowerCase().includes(term) ||
            this.phone.toLowerCase().includes(term) ||
            this.email.toLowerCase().includes(term) || 
            this.address.toLowerCase().includes(term)
        )
    }

    /**
     * @param {number} id
     */
    hasLabel(id) {
        return this.labels?.some((label) => label.id == id);
    }
}