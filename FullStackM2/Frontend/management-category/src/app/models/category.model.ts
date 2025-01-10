
export class Category {
  id?: number;
  nom: string;
  parentId?: number | null;
  parentCategory?: Category[] | null;
  childCategories?: Category[];
  root: boolean;
  creationDate?: string;

  constructor() {
    this.nom = '';
    this.childCategories = [];
    this.parentCategory = null;
    this.root = false;
    this.creationDate = '';
  }
}
