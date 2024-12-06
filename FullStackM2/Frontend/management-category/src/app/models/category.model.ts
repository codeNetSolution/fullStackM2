// category.model.ts
export class Category {
  id?: number;
  nom: string;
  parentId?: number | null;
  childCategories?: Category[];
  isRoot: boolean;

  constructor() {
    this.nom = '';
    this.childCategories = [];
    this.isRoot = false;
  }
}
