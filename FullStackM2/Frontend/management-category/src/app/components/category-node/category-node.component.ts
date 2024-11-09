import { Component, Input } from '@angular/core';
import { Category } from '../../models/category.model';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-node',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './category-node.component.html',
})
export class CategoryNodeComponent {
  @Input() category!: Category;

  deleteCategory(id: number): void {
    console.log(`Suppression de la catégorie avec ID: ${id}`);
    // Call to parent or service to delete the category
  }
}
