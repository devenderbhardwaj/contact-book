

export class DeleteDialog {
    element;
	#onYes
	/**
	 * @param {Function} successcallBack
	 */
    constructor(onyes) {
        this.#onYes = onyes;
        this.element = document.createElement("dialog");
        this.element.className = "delete";
        this.element.innerHTML = (
            `
            <form method="dialog">
                <p>Delete Contact</p>
                <div class='controls'>
                    <button value="No">No</button>
                    <button value="Yes">Ok</button>
                </div>
            </form>
            `
        );
        this.element.addEventListener("close", () => {
            if (this.element.returnValue == "Yes") {
                this.#onYes();
            }
        })

        document.body.append(this.element);
    }

    show() {
        this.element.showModal();
    }

}


