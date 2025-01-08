// category.model.ts
export class Category {
  id?: number;
  nom: string;
  parentId?: number | null;
  childCategories?: Category[];
  root: boolean;

  constructor() {
    this.nom = '';
    this.childCategories = [];
    this.root = false;
  }
}
