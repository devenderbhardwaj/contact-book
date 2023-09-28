import Label from "../Model/Label.js";
import LabelService from "../Model/LabelsService.js";
import { MainContentArea } from "./MainContentArea.js";
import { SideBarView } from "./SideBarView.js";

export class MainView {
    #element;
    #sidebar;
    #mainContentArea;
    #labelsService;

    constructor() {
        this.#element = document.createElement("main");
        this.#element.className = "main";

        this.#sidebar = new SideBarView();
        this.#sidebar.bindOnContactCreate(this.#onCreateConact);
        this.#mainContentArea = new MainContentArea();

        this.#element.append(this.#sidebar.getViewElement());
        this.#element.append(this.#mainContentArea.getViewElement());

        this.#labelsService = LabelService;
        this.#labelsService.bindOnLoad(this.#onLabelsLoad);
        this.#labelsService.refresh();
    }

    getViewElement() {
        return this.#element;
    }

    #onCreateConact = () => {
        this.#mainContentArea.createContact();
    }

    search = (term) => {
        this.#mainContentArea.search(term);
    }

    /**
     * 
     * @param {Label[]} labels 
     */
    #onLabelsLoad = (labels) => {
        this.#sidebar.update(labels);
    }
}