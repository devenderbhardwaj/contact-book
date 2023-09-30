export class HeaderView {
    #element;

    #search;

    constructor() {
        /**
        * @property {HTMLElement}
        */
        this.#element = document.createElement("header");
        this.#element.innerHTML = (
            `
            <button type="button" class="menu">Menu</button>
            <input type="search" name="filter-input" class="filter" placeholder='Search'>
            <button type="button" class="log-out">Log out</button>
            `
        );
        this.#element.querySelector("button.menu").addEventListener("click", () => {
            document.body.classList.add("fixed-sidebar-show");
        })
        this.#element.querySelector("input").addEventListener("input", (e) => this.#search(e.target.value));
        this.#element.querySelector(".log-out").addEventListener("click", this.logout);
    }

    /**
     * 
     * @param {Function} callBack 
     */
    bindOnSearch(callBack) {
        this.#search = callBack;
    }
    getViewElement() {
        return this.#element
    }

    logout() {
        const request = new XMLHttpRequest();
        request.open("GET", "logout");
        request.onload = () => {
            window.location.href = request.responseURL
        }
        request.send();
    }
}