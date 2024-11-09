import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-form',
  standalone: true,
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css'],
  imports: [FormsModule, CommonModule] // Ajoutez FormsModule ici
})
export class CategoryFormComponent implements OnInit {
  category: Category = new Category();

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {}

  saveCategory(): void {
    this.categoryService.createCategory(this.category).subscribe(
      (response) => {
        console.log('Catégorie créée avec succès', response);
      },
      (error) => {
        console.error('Erreur lors de la création de la catégorie', error);
      }
    );
  }
}
