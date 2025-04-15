import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Product } from './product';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private httpClient = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/v1/products';

  getProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.baseUrl);
  }

  addProduct(product: Omit<Product, 'id'>): Observable<Product> {
    return this.httpClient.post<Product>(this.baseUrl, product);
  }

  editProduct(id: number, product: Product): Observable<Product> {
    return this.httpClient.put<Product>(`${this.baseUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${id}`);
  }
}
