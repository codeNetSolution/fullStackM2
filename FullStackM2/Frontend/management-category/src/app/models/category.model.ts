// category.model.ts
export class Category {
  id?: number;
  nom: string;
  parentId?: number;
  childCategories?: Category[];

  constructor() {
    this.nom = '';
    this.childCategories = [];
  }
}
