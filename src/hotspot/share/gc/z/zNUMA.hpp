/*
 * Copyright (c) 2016, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

#ifndef SHARE_GC_Z_ZNUMA_HPP
#define SHARE_GC_Z_ZNUMA_HPP

#include "gc/z/zGlobals.hpp"
#include "memory/allStatic.hpp"
#include "utilities/globalDefinitions.hpp"

class ZNUMA : public AllStatic {
  friend class VMStructs;
  friend class ZNUMATest;
  friend class ZTest;

private:
  static bool     _enabled;
  static uint32_t _count;

  static void pd_initialize();

public:
  static void initialize();

  static bool is_enabled();
  static bool is_faked();

  static uint32_t count();
  static uint32_t id();

  static uint32_t memory_id(uintptr_t addr);

  static size_t calculate_share(uint32_t numa_id, size_t total, size_t granule = ZGranuleSize, uint32_t ignore_count = 0);

  static const char* to_string();
};

#endif // SHARE_GC_Z_ZNUMA_HPP
