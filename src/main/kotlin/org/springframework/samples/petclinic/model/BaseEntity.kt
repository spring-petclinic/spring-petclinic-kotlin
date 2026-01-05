/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model

import java.io.Serializable
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Antoine Rey
 */
// このクラス自体はテーブルにならない
// 継承先の共通フィールド定義
@MappedSuperclass
// kotlinのクラスはデフォルトで継承不可(final)
// 継承可能にするにはopen修飾子をつける
open class BaseEntity : Serializable {

    // 主キー
    @Id
    // データベースのオートインクリメント
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // varならgetter/setterが自動生成される
    open var id: Int? = null

    // valはgetterのみ自動生成される
    // これはカスタムgetter
    val isNew: Boolean
        // entity.isNew で呼び出す
        get() = this.id == null

}
