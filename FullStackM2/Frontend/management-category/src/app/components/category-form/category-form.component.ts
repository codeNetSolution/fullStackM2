import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-category-form',
  standalone: true, 
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit {
  categoryForm!: FormGroup;
  categoryId?: number;
  isEditMode = false;

  constructor(
    private formBuilder: FormBuilder,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.formBuilder.group({
      nom: ['', Validators.required]
    });

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.categoryId = +params['id'];
        this.categoryService.getCategoryDetails(this.categoryId).subscribe(
          (category) => this.categoryForm.patchValue(category)
        );
      }
    });
  }

  onSubmit(): void {
    if (this.categoryForm.invalid) {
      return;
    }
  
    const category: Category = this.categoryForm.value;
  
    if (this.isEditMode && this.categoryId) {
      // Mode édition
      this.categoryService.updateCategory(this.categoryId, category).subscribe({
        next: () => this.router.navigate(['/categories']),
        error: (error) => console.error('Erreur lors de la mise à jour de la catégorie', error)
      });
    } else {
      // Mode création
      this.categoryService.createCategory(category).subscribe({
        next: () => this.router.navigate(['/categories']),
        error: (error) => console.error('Erreur lors de la création de la catégorie', error)
      });
    }
  }
  
}
