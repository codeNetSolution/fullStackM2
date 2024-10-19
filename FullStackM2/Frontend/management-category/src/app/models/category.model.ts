export interface Category {
    id?: number;
    nom: string;
    parentCategory?: Category;
    childCategories?: Category[];
    createdAt?: Date;
}