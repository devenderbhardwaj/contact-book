export default class Label {
    /**
     * @prop {number}
     */
    id;

    /**
     * @prop {string}
     */
    text;

    /**
     * 
     * @param {{id:number, text:string}} 
     */
    constructor({id , text}) {
        this.id = id;
        this.text = text;
    }
}