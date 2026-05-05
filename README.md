# Bandage E-Commerce Backend

Spring Boot ile geliştirilmiş REST API. Workintech e-commerce ödevindeki T08–T23 issue akışlarını ve React frontend uygulamasının beklediği endpoint/response formatlarını destekler.

---

## Proje Durumu

Temel e-commerce akışları tamamlanmıştır:

- Kullanıcı kaydı ve girişi
- JWT token doğrulama
- Rol listeleme
- Kategori listeleme
- Ürün listeleme (filtreleme, sıralama, sayfalama)
- Ürün detay bilgisi
- Adres CRUD işlemleri
- Kredi kartı CRUD işlemleri
- Sipariş oluşturma
- Geçmiş siparişleri listeleme

---

## Teknolojiler

| Katman | Teknoloji |
|---|---|
| Dil | Java 17 |
| Framework | Spring Boot 3.2.1 |
| Güvenlik | Spring Security, JWT, BCrypt |
| Veritabanı | PostgreSQL (prod), H2 (test) |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |

---

## Kurulum ve Çalıştırma

### 1. PostgreSQL veritabanı oluştur

```sql
CREATE DATABASE bandage_ecommerce;
```

### 2. Ortam değişkenlerini ayarla

Gerçek şifreler GitHub'a yüklenmez. Ortam değişkeni olarak verilir.

**PowerShell:**
```powershell
$env:DB_PASSWORD="kendi_postgresql_sifreniz"
$env:JWT_SECRET="uzun-ve-gizli-bir-jwt-anahtari"
```

İsteğe bağlı olarak şunlar da değiştirilebilir:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/bandage_ecommerce"
$env:DB_USERNAME="postgres"
```

### 3. Uygulamayı başlat

```bash
cd e-commerce-backend
.\mvnw.cmd spring-boot:run
```

Backend varsayılan olarak şu adreste çalışır: `http://localhost:8080`

---

## Application Properties

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/bandage_ecommerce}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-minutes=${JWT_EXPIRATION_MINUTES:1440}
app.frontend.origin=${FRONTEND_ORIGIN:http://localhost:5173}
```

---

## Test Kullanıcıları

Uygulama ilk çalıştığında seed data otomatik oluşturulur:

| Email | Şifre | Rol |
|---|---|---|
| customer@commerce.com | 123456 | Customer |
| store@commerce.com | 123456 | Store |
| admin@commerce.com | 123456 | Admin |

---

## Token Kullanımı

Issue gereksinimlerine uygun olarak token, header içinde doğrudan gönderilir. `Bearer` prefix'i **kullanılmaz**.

```
Authorization: JWT_TOKEN
```

---

## API Endpointleri

### Auth & Roller

| Method | Endpoint | Açıklama |
|---|---|---|
| GET | `/roles` | Rolleri listeler |
| POST | `/signup` | Yeni kullanıcı oluşturur |
| POST | `/login` | Kullanıcı girişi yapar |
| GET | `/verify` | Token doğrular, kullanıcı bilgisi döner |

### Ürün & Kategori

| Method | Endpoint | Açıklama |
|---|---|---|
| GET | `/categories` | Kategorileri listeler |
| GET | `/products` | Ürünleri listeler |
| GET | `/products/{productId}` | Tek ürün detayını getirir |

### Adres

| Method | Endpoint | Açıklama |
|---|---|---|
| GET | `/user/address` | Kullanıcı adreslerini listeler |
| POST | `/user/address` | Yeni adres ekler |
| PUT | `/user/address` | Adres günceller |
| DELETE | `/user/address/{addressId}` | Adres siler |

### Kart

| Method | Endpoint | Açıklama |
|---|---|---|
| GET | `/user/card` | Kullanıcı kartlarını listeler |
| POST | `/user/card` | Yeni kart ekler |
| PUT | `/user/card` | Kart günceller |
| DELETE | `/user/card/{cardId}` | Kart siler |

### Sipariş

| Method | Endpoint | Açıklama |
|---|---|---|
| POST | `/order` | Sipariş oluşturur |
| GET | `/order` | Geçmiş siparişleri listeler |

---

## Ürün Listeleme Query Parametreleri

`GET /products` aşağıdaki parametreleri destekler:

| Parametre | Açıklama |
|---|---|
| `category` | Kategori ID'ye göre filtreler |
| `filter` | Ürün adında metin araması yapar |
| `sort` | `price:asc`, `price:desc`, `rating:asc`, `rating:desc` |
| `limit` | Sayfa başına ürün sayısı |
| `offset` | Kaçıncı üründen başlanacağı |

**Örnekler:**
```
GET /products?limit=25&offset=0
GET /products?category=3
GET /products?filter=gri
GET /products?sort=price:desc
GET /products?category=3&filter=gri&sort=rating:desc&limit=25&offset=0
```

**Response formatı:**
```json
{
  "total": 25,
  "products": []
}
```

---

## Görseller

Ürün görselleri backend tarafında local static dosyalar üzerinden servis edilir.

```
http://localhost:8080/images/products/product-1.jpg
```

Ürün response'unda görseller şu formatta döner:

```json
"images": [
  {
    "url": "http://localhost:8080/images/products/product-1.jpg",
    "index": 0
  }
]
```

---

## Test Edilen Akışlar

Teslim öncesi Postman ve frontend ile kontrol edilmiştir:

- `GET /categories`
- `GET /products`
- `GET /products/{productId}`
- `POST /login` ve `GET /verify`
- Adres ekleme, güncelleme, silme
- Kart ekleme, güncelleme, silme
- Sipariş oluşturma
- Geçmiş siparişleri listeleme
- Frontend ile login, sepet, order ve previous orders akışı

---

## Güvenlik Notları

- Gerçek veritabanı şifresi GitHub'a yüklenmez.
- JWT secret GitHub'a yüklenmez.
- Kredi kartı CVV bilgisi saklanmaz.
- Bu proje eğitim/ödev kapsamında demo amaçlıdır. Gerçek production ortamında kart numaraları ve ödeme süreci için ek güvenlik katmanları gerekir.

---

## Yazar

**İsmail Avşar** — [@ismail-avsar](https://github.com/ismail-avsar)