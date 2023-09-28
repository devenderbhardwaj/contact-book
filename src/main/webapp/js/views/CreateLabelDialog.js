export class CreateLabelDialog {
    element;
	#onYes

	/**
	 * @param {Function} successcallBack
	 */
    constructor(onyes) {
        this.#onYes = onyes;
        this.element = document.createElement("dialog");
        this.element.className = "delete createLabel";
        this.element.innerHTML = (
            `
            <form method="dialog">
                <label><input type='text' name='label-text' placeholder='Label'></label>
                <div class='controls'>
                    <button value="No">Cancel</button>
                    <button value="Yes">Create</button>
                </div>
            </form>
            `
        );
        this.element.addEventListener("close", () => {
            if (this.element.returnValue == "Yes") {
                this.#onYes(this.element.querySelector("input").value);
            }
            this.element.remove();
        })

        document.body.append(this.element);
    }

    show() {
        this.element.showModal();
    }

}


