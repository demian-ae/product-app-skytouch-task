import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ProductService } from './product.service';
import { HttpClient } from '@angular/common/http';
import { Product } from './product';

describe('ProductService', () => {
  let service: ProductService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const mockProducts: Product[] = [
    { id: 1, name: 'Apple', description: 'Red apple', price: 1.99, expirationDate: new Date('2025-01-01') },
    { id: 2, name: 'Banana', description: 'Yellow banana', price: 0.99, expirationDate: new Date('2025-02-01') }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ]
    });

    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(ProductService);
  });
  
  afterEach(() => {
    httpTestingController.verify(); // ensure no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all products', () => {
    service.getProducts().subscribe(products => {
      expect(products.length).toBe(2);
      expect(products).toEqual(mockProducts);
    });
    
    const req = httpTestingController.expectOne('http://localhost:8080/api/v1/products');
    expect(req.request.method).toEqual('GET');
    req.flush(mockProducts);
  });

  it('should add a product', () => {
    const newProduct = {
      name: 'Orange',
      description: 'Juicy orange',
      price: 1.5,
      expirationDate: new Date('2025-03-01')
    };

    const expectedProduct: Product = { id: 3, ...newProduct };

    service.addProduct(newProduct).subscribe(result => {
      expect(result).toEqual(expectedProduct);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/api/v1/products');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(newProduct);
    req.flush(expectedProduct);
  });

  it('should edit a product', () => {
    const updatedProduct: Product = {
      id: 1,
      name: 'Updated Apple',
      description: 'Green apple',
      price: 2.5,
      expirationDate: new Date('2025-04-01')
    };

    service.editProduct(updatedProduct.id, updatedProduct).subscribe(result => {
      expect(result).toEqual(updatedProduct);
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/api/v1/products/1`);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(updatedProduct);
    req.flush(updatedProduct);
  });

  it('should delete a product', () => {
    const productId = 1;

    service.deleteProduct(productId).subscribe(result => {
      expect(result).toBeNull(); // DELETE returns void
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/api/v1/products/1`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });
});
