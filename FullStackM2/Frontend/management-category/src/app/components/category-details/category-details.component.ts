import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import { HttpClientModule } from '@angular/common/http';

import { Category } from '../../models/category.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-details',
  standalone: true,
  templateUrl: './category-details.component.html',
  imports: [HttpClientModule,CommonModule],
  styleUrls: ['./category-details.component.css']
})
export class CategoryDetailsComponent implements OnInit {
  category?: Category;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.categoryService.getCategoryDetails(id).subscribe(
      (category) => this.category = category
    );
  }
}
