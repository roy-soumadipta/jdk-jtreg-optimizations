/*
 * Copyright (c) 2017, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package jdk.incubator.vector;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

import jdk.internal.vm.annotation.ForceInline;
import jdk.internal.vm.vector.VectorSupport;

import static jdk.internal.vm.vector.VectorSupport.*;

import static jdk.incubator.vector.VectorOperators.*;

// -- This file was mechanically generated: Do not edit! -- //

@SuppressWarnings("cast")  // warning: redundant cast
final class Short256Vector extends ShortVector {
    static final ShortSpecies VSPECIES =
        (ShortSpecies) ShortVector.SPECIES_256;

    static final VectorShape VSHAPE =
        VSPECIES.vectorShape();

    static final Class<Short256Vector> VCLASS = Short256Vector.class;

    static final int VSIZE = VSPECIES.vectorBitSize();

    static final int VLENGTH = VSPECIES.laneCount(); // used by the JVM

    static final Class<Short> ETYPE = short.class; // used by the JVM

    Short256Vector(short[] v) {
        super(v);
    }

    // For compatibility as Short256Vector::new,
    // stored into species.vectorFactory.
    Short256Vector(Object v) {
        this((short[]) v);
    }

    static final Short256Vector ZERO = new Short256Vector(new short[VLENGTH]);
    static final Short256Vector IOTA = new Short256Vector(VSPECIES.iotaArray());

    static {
        // Warm up a few species caches.
        // If we do this too much we will
        // get NPEs from bootstrap circularity.
        VSPECIES.dummyVector();
        VSPECIES.withLanes(LaneType.BYTE);
    }

    // Specialized extractors

    @ForceInline
    final @Override
    public ShortSpecies vspecies() {
        // ISSUE:  This should probably be a @Stable
        // field inside AbstractVector, rather than
        // a megamorphic method.
        return VSPECIES;
    }

    @ForceInline
    @Override
    public final Class<Short> elementType() { return short.class; }

    @ForceInline
    @Override
    public final int elementSize() { return Short.SIZE; }

    @ForceInline
    @Override
    public final VectorShape shape() { return VSHAPE; }

    @ForceInline
    @Override
    public final int length() { return VLENGTH; }

    @ForceInline
    @Override
    public final int bitSize() { return VSIZE; }

    @ForceInline
    @Override
    public final int byteSize() { return VSIZE / Byte.SIZE; }

    /*package-private*/
    @ForceInline
    final @Override
    short[] vec() {
        return (short[])getPayload();
    }

    // Virtualized constructors

    @Override
    @ForceInline
    public final Short256Vector broadcast(short e) {
        return (Short256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    public final Short256Vector broadcast(long e) {
        return (Short256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    Short256Mask maskFromArray(boolean[] bits) {
        return new Short256Mask(bits);
    }

    @Override
    @ForceInline
    Short256Shuffle iotaShuffle() { return Short256Shuffle.IOTA; }

    @Override
    @ForceInline
    Short256Shuffle iotaShuffle(int start, int step, boolean wrap) {
        return (Short256Shuffle) iotaShuffleTemplate((short) start, (short) step, wrap);
    }

    @Override
    @ForceInline
    Short256Shuffle shuffleFromArray(int[] indices, int i) { return new Short256Shuffle(indices, i); }

    @Override
    @ForceInline
    Short256Shuffle shuffleFromOp(IntUnaryOperator fn) { return new Short256Shuffle(fn); }

    // Make a vector of the same species but the given elements:
    @ForceInline
    final @Override
    Short256Vector vectorFactory(short[] vec) {
        return new Short256Vector(vec);
    }

    @ForceInline
    final @Override
    Byte256Vector asByteVectorRaw() {
        return (Byte256Vector) super.asByteVectorRawTemplate();  // specialize
    }

    @ForceInline
    final @Override
    AbstractVector<?> asVectorRaw(LaneType laneType) {
        return super.asVectorRawTemplate(laneType);  // specialize
    }

    // Unary operator

    @ForceInline
    final @Override
    Short256Vector uOp(FUnOp f) {
        return (Short256Vector) super.uOpTemplate(f);  // specialize
    }

    @ForceInline
    final @Override
    Short256Vector uOp(VectorMask<Short> m, FUnOp f) {
        return (Short256Vector)
            super.uOpTemplate((Short256Mask)m, f);  // specialize
    }

    // Binary operator

    @ForceInline
    final @Override
    Short256Vector bOp(Vector<Short> v, FBinOp f) {
        return (Short256Vector) super.bOpTemplate((Short256Vector)v, f);  // specialize
    }

    @ForceInline
    final @Override
    Short256Vector bOp(Vector<Short> v,
                     VectorMask<Short> m, FBinOp f) {
        return (Short256Vector)
            super.bOpTemplate((Short256Vector)v, (Short256Mask)m,
                              f);  // specialize
    }

    // Ternary operator

    @ForceInline
    final @Override
    Short256Vector tOp(Vector<Short> v1, Vector<Short> v2, FTriOp f) {
        return (Short256Vector)
            super.tOpTemplate((Short256Vector)v1, (Short256Vector)v2,
                              f);  // specialize
    }

    @ForceInline
    final @Override
    Short256Vector tOp(Vector<Short> v1, Vector<Short> v2,
                     VectorMask<Short> m, FTriOp f) {
        return (Short256Vector)
            super.tOpTemplate((Short256Vector)v1, (Short256Vector)v2,
                              (Short256Mask)m, f);  // specialize
    }

    @ForceInline
    final @Override
    short rOp(short v, VectorMask<Short> m, FBinOp f) {
        return super.rOpTemplate(v, m, f);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> convertShape(VectorOperators.Conversion<Short,F> conv,
                           VectorSpecies<F> rsp, int part) {
        return super.convertShapeTemplate(conv, rsp, part);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> reinterpretShape(VectorSpecies<F> toSpecies, int part) {
        return super.reinterpretShapeTemplate(toSpecies, part);  // specialize
    }

    // Specialized algebraic operations:

    // The following definition forces a specialized version of this
    // crucial method into the v-table of this class.  A call to add()
    // will inline to a call to lanewise(ADD,), at which point the JIT
    // intrinsic will have the opcode of ADD, plus all the metadata
    // for this particular class, enabling it to generate precise
    // code.
    //
    // There is probably no benefit to the JIT to specialize the
    // masked or broadcast versions of the lanewise method.

    @Override
    @ForceInline
    public Short256Vector lanewise(Unary op) {
        return (Short256Vector) super.lanewiseTemplate(op);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector lanewise(Unary op, VectorMask<Short> m) {
        return (Short256Vector) super.lanewiseTemplate(op, Short256Mask.class, (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector lanewise(Binary op, Vector<Short> v) {
        return (Short256Vector) super.lanewiseTemplate(op, v);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector lanewise(Binary op, Vector<Short> v, VectorMask<Short> m) {
        return (Short256Vector) super.lanewiseTemplate(op, Short256Mask.class, v, (Short256Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Short256Vector
    lanewiseShift(VectorOperators.Binary op, int e) {
        return (Short256Vector) super.lanewiseShiftTemplate(op, e);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Short256Vector
    lanewiseShift(VectorOperators.Binary op, int e, VectorMask<Short> m) {
        return (Short256Vector) super.lanewiseShiftTemplate(op, Short256Mask.class, e, (Short256Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline
    public final
    Short256Vector
    lanewise(Ternary op, Vector<Short> v1, Vector<Short> v2) {
        return (Short256Vector) super.lanewiseTemplate(op, v1, v2);  // specialize
    }

    @Override
    @ForceInline
    public final
    Short256Vector
    lanewise(Ternary op, Vector<Short> v1, Vector<Short> v2, VectorMask<Short> m) {
        return (Short256Vector) super.lanewiseTemplate(op, Short256Mask.class, v1, v2, (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public final
    Short256Vector addIndex(int scale) {
        return (Short256Vector) super.addIndexTemplate(scale);  // specialize
    }

    // Type specific horizontal reductions

    @Override
    @ForceInline
    public final short reduceLanes(VectorOperators.Associative op) {
        return super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final short reduceLanes(VectorOperators.Associative op,
                                    VectorMask<Short> m) {
        return super.reduceLanesTemplate(op, Short256Mask.class, (Short256Mask) m);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op) {
        return (long) super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op,
                                        VectorMask<Short> m) {
        return (long) super.reduceLanesTemplate(op, Short256Mask.class, (Short256Mask) m);  // specialized
    }

    @Override
    @ForceInline
    final <F> VectorShuffle<F> bitsToShuffle(AbstractSpecies<F> dsp) {
        return bitsToShuffleTemplate(dsp);
    }

    @Override
    @ForceInline
    public final Short256Shuffle toShuffle() {
        return (Short256Shuffle) toShuffle(vspecies(), false);
    }

    // Specialized unary testing

    @Override
    @ForceInline
    public final Short256Mask test(Test op) {
        return super.testTemplate(Short256Mask.class, op);  // specialize
    }

    @Override
    @ForceInline
    public final Short256Mask test(Test op, VectorMask<Short> m) {
        return super.testTemplate(Short256Mask.class, op, (Short256Mask) m);  // specialize
    }

    // Specialized comparisons

    @Override
    @ForceInline
    public final Short256Mask compare(Comparison op, Vector<Short> v) {
        return super.compareTemplate(Short256Mask.class, op, v);  // specialize
    }

    @Override
    @ForceInline
    public final Short256Mask compare(Comparison op, short s) {
        return super.compareTemplate(Short256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Short256Mask compare(Comparison op, long s) {
        return super.compareTemplate(Short256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Short256Mask compare(Comparison op, Vector<Short> v, VectorMask<Short> m) {
        return super.compareTemplate(Short256Mask.class, op, v, (Short256Mask) m);
    }


    @Override
    @ForceInline
    public Short256Vector blend(Vector<Short> v, VectorMask<Short> m) {
        return (Short256Vector)
            super.blendTemplate(Short256Mask.class,
                                (Short256Vector) v,
                                (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector slice(int origin, Vector<Short> v) {
        return (Short256Vector) super.sliceTemplate(origin, v);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector slice(int origin) {
        return (Short256Vector) super.sliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector unslice(int origin, Vector<Short> w, int part) {
        return (Short256Vector) super.unsliceTemplate(origin, w, part);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector unslice(int origin, Vector<Short> w, int part, VectorMask<Short> m) {
        return (Short256Vector)
            super.unsliceTemplate(Short256Mask.class,
                                  origin, w, part,
                                  (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector unslice(int origin) {
        return (Short256Vector) super.unsliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector rearrange(VectorShuffle<Short> s) {
        return (Short256Vector)
            super.rearrangeTemplate(Short256Shuffle.class,
                                    (Short256Shuffle) s);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector rearrange(VectorShuffle<Short> shuffle,
                                  VectorMask<Short> m) {
        return (Short256Vector)
            super.rearrangeTemplate(Short256Shuffle.class,
                                    Short256Mask.class,
                                    (Short256Shuffle) shuffle,
                                    (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector rearrange(VectorShuffle<Short> s,
                                  Vector<Short> v) {
        return (Short256Vector)
            super.rearrangeTemplate(Short256Shuffle.class,
                                    (Short256Shuffle) s,
                                    (Short256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector compress(VectorMask<Short> m) {
        return (Short256Vector)
            super.compressTemplate(Short256Mask.class,
                                   (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector expand(VectorMask<Short> m) {
        return (Short256Vector)
            super.expandTemplate(Short256Mask.class,
                                   (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector selectFrom(Vector<Short> v) {
        return (Short256Vector)
            super.selectFromTemplate((Short256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector selectFrom(Vector<Short> v,
                                   VectorMask<Short> m) {
        return (Short256Vector)
            super.selectFromTemplate((Short256Vector) v,
                                     Short256Mask.class, (Short256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Short256Vector selectFrom(Vector<Short> v1,
                                   Vector<Short> v2) {
        return (Short256Vector)
            super.selectFromTemplate((Short256Vector) v1, (Short256Vector) v2);  // specialize
    }

    @ForceInline
    @Override
    public short lane(int i) {
        switch(i) {
            case 0: return laneHelper(0);
            case 1: return laneHelper(1);
            case 2: return laneHelper(2);
            case 3: return laneHelper(3);
            case 4: return laneHelper(4);
            case 5: return laneHelper(5);
            case 6: return laneHelper(6);
            case 7: return laneHelper(7);
            case 8: return laneHelper(8);
            case 9: return laneHelper(9);
            case 10: return laneHelper(10);
            case 11: return laneHelper(11);
            case 12: return laneHelper(12);
            case 13: return laneHelper(13);
            case 14: return laneHelper(14);
            case 15: return laneHelper(15);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    @ForceInline
    public short laneHelper(int i) {
        return (short) VectorSupport.extract(
                                VCLASS, ETYPE, VLENGTH,
                                this, i,
                                (vec, ix) -> {
                                    short[] vecarr = vec.vec();
                                    return (long)vecarr[ix];
                                });
    }

    @ForceInline
    @Override
    public Short256Vector withLane(int i, short e) {
        switch (i) {
            case 0: return withLaneHelper(0, e);
            case 1: return withLaneHelper(1, e);
            case 2: return withLaneHelper(2, e);
            case 3: return withLaneHelper(3, e);
            case 4: return withLaneHelper(4, e);
            case 5: return withLaneHelper(5, e);
            case 6: return withLaneHelper(6, e);
            case 7: return withLaneHelper(7, e);
            case 8: return withLaneHelper(8, e);
            case 9: return withLaneHelper(9, e);
            case 10: return withLaneHelper(10, e);
            case 11: return withLaneHelper(11, e);
            case 12: return withLaneHelper(12, e);
            case 13: return withLaneHelper(13, e);
            case 14: return withLaneHelper(14, e);
            case 15: return withLaneHelper(15, e);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    @ForceInline
    public Short256Vector withLaneHelper(int i, short e) {
        return VectorSupport.insert(
                                VCLASS, ETYPE, VLENGTH,
                                this, i, (long)e,
                                (v, ix, bits) -> {
                                    short[] res = v.vec().clone();
                                    res[ix] = (short)bits;
                                    return v.vectorFactory(res);
                                });
    }

    // Mask

    static final class Short256Mask extends AbstractMask<Short> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Short> ETYPE = short.class; // used by the JVM

        Short256Mask(boolean[] bits) {
            this(bits, 0);
        }

        Short256Mask(boolean[] bits, int offset) {
            super(prepare(bits, offset));
        }

        Short256Mask(boolean val) {
            super(prepare(val));
        }

        private static boolean[] prepare(boolean[] bits, int offset) {
            boolean[] newBits = new boolean[VSPECIES.laneCount()];
            for (int i = 0; i < newBits.length; i++) {
                newBits[i] = bits[offset + i];
            }
            return newBits;
        }

        private static boolean[] prepare(boolean val) {
            boolean[] bits = new boolean[VSPECIES.laneCount()];
            Arrays.fill(bits, val);
            return bits;
        }

        @ForceInline
        final @Override
        public ShortSpecies vspecies() {
            // ISSUE:  This should probably be a @Stable
            // field inside AbstractMask, rather than
            // a megamorphic method.
            return VSPECIES;
        }

        @ForceInline
        boolean[] getBits() {
            return (boolean[])getPayload();
        }

        @Override
        Short256Mask uOp(MUnOp f) {
            boolean[] res = new boolean[vspecies().laneCount()];
            boolean[] bits = getBits();
            for (int i = 0; i < res.length; i++) {
                res[i] = f.apply(i, bits[i]);
            }
            return new Short256Mask(res);
        }

        @Override
        Short256Mask bOp(VectorMask<Short> m, MBinOp f) {
            boolean[] res = new boolean[vspecies().laneCount()];
            boolean[] bits = getBits();
            boolean[] mbits = ((Short256Mask)m).getBits();
            for (int i = 0; i < res.length; i++) {
                res[i] = f.apply(i, bits[i], mbits[i]);
            }
            return new Short256Mask(res);
        }

        @ForceInline
        @Override
        public final
        Short256Vector toVector() {
            return (Short256Vector) super.toVectorTemplate();  // specialize
        }

        /**
         * Helper function for lane-wise mask conversions.
         * This function kicks in after intrinsic failure.
         */
        @ForceInline
        private final <E>
        VectorMask<E> defaultMaskCast(AbstractSpecies<E> dsp) {
            if (length() != dsp.laneCount())
                throw new IllegalArgumentException("VectorMask length and species length differ");
            boolean[] maskArray = toArray();
            return  dsp.maskFactory(maskArray).check(dsp);
        }

        @Override
        @ForceInline
        public <E> VectorMask<E> cast(VectorSpecies<E> dsp) {
            AbstractSpecies<E> species = (AbstractSpecies<E>) dsp;
            if (length() != species.laneCount())
                throw new IllegalArgumentException("VectorMask length and species length differ");

            return VectorSupport.convert(VectorSupport.VECTOR_OP_CAST,
                this.getClass(), ETYPE, VLENGTH,
                species.maskType(), species.elementType(), VLENGTH,
                this, species,
                (m, s) -> s.maskFactory(m.toArray()).check(s));
        }

        @Override
        @ForceInline
        /*package-private*/
        Short256Mask indexPartiallyInUpperRange(long offset, long limit) {
            return (Short256Mask) VectorSupport.indexPartiallyInUpperRange(
                Short256Mask.class, short.class, VLENGTH, offset, limit,
                (o, l) -> (Short256Mask) TRUE_MASK.indexPartiallyInRange(o, l));
        }

        // Unary operations

        @Override
        @ForceInline
        public Short256Mask not() {
            return xor(maskAll(true));
        }

        @Override
        @ForceInline
        public Short256Mask compress() {
            return (Short256Mask)VectorSupport.compressExpandOp(VectorSupport.VECTOR_OP_MASK_COMPRESS,
                Short256Vector.class, Short256Mask.class, ETYPE, VLENGTH, null, this,
                (v1, m1) -> VSPECIES.iota().compare(VectorOperators.LT, m1.trueCount()));
        }


        // Binary operations

        @Override
        @ForceInline
        public Short256Mask and(VectorMask<Short> mask) {
            Objects.requireNonNull(mask);
            Short256Mask m = (Short256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_AND, Short256Mask.class, null, short.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a & b));
        }

        @Override
        @ForceInline
        public Short256Mask or(VectorMask<Short> mask) {
            Objects.requireNonNull(mask);
            Short256Mask m = (Short256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_OR, Short256Mask.class, null, short.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a | b));
        }

        @Override
        @ForceInline
        public Short256Mask xor(VectorMask<Short> mask) {
            Objects.requireNonNull(mask);
            Short256Mask m = (Short256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_XOR, Short256Mask.class, null, short.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a ^ b));
        }

        // Mask Query operations

        @Override
        @ForceInline
        public int trueCount() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TRUECOUNT, Short256Mask.class, short.class, VLENGTH, this,
                                                      (m) -> trueCountHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public int firstTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_FIRSTTRUE, Short256Mask.class, short.class, VLENGTH, this,
                                                      (m) -> firstTrueHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public int lastTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_LASTTRUE, Short256Mask.class, short.class, VLENGTH, this,
                                                      (m) -> lastTrueHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public long toLong() {
            if (length() > Long.SIZE) {
                throw new UnsupportedOperationException("too many lanes for one long");
            }
            return VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TOLONG, Short256Mask.class, short.class, VLENGTH, this,
                                                      (m) -> toLongHelper(m.getBits()));
        }

        // laneIsSet

        @Override
        @ForceInline
        public boolean laneIsSet(int i) {
            Objects.checkIndex(i, length());
            return VectorSupport.extract(Short256Mask.class, short.class, VLENGTH,
                                         this, i, (m, idx) -> (m.getBits()[idx] ? 1L : 0L)) == 1L;
        }

        // Reductions

        @Override
        @ForceInline
        public boolean anyTrue() {
            return VectorSupport.test(BT_ne, Short256Mask.class, short.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> anyTrueHelper(((Short256Mask)m).getBits()));
        }

        @Override
        @ForceInline
        public boolean allTrue() {
            return VectorSupport.test(BT_overflow, Short256Mask.class, short.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> allTrueHelper(((Short256Mask)m).getBits()));
        }

        @ForceInline
        /*package-private*/
        static Short256Mask maskAll(boolean bit) {
            return VectorSupport.fromBitsCoerced(Short256Mask.class, short.class, VLENGTH,
                                                 (bit ? -1 : 0), MODE_BROADCAST, null,
                                                 (v, __) -> (v != 0 ? TRUE_MASK : FALSE_MASK));
        }
        private static final Short256Mask  TRUE_MASK = new Short256Mask(true);
        private static final Short256Mask FALSE_MASK = new Short256Mask(false);

    }

    // Shuffle

    static final class Short256Shuffle extends AbstractShuffle<Short> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Short> ETYPE = short.class; // used by the JVM

        Short256Shuffle(short[] indices) {
            super(indices);
            assert(VLENGTH == indices.length);
            assert(indicesInRange(indices));
        }

        Short256Shuffle(int[] indices, int i) {
            this(prepare(indices, i));
        }

        Short256Shuffle(IntUnaryOperator fn) {
            this(prepare(fn));
        }

        short[] indices() {
            return (short[])getPayload();
        }

        @Override
        @ForceInline
        public ShortSpecies vspecies() {
            return VSPECIES;
        }

        static {
            // There must be enough bits in the shuffle lanes to encode
            // VLENGTH valid indexes and VLENGTH exceptional ones.
            assert(VLENGTH < Short.MAX_VALUE);
            assert(Short.MIN_VALUE <= -VLENGTH);
        }
        static final Short256Shuffle IOTA = new Short256Shuffle(IDENTITY);

        @Override
        @ForceInline
        public Short256Vector toVector() {
            return toBitsVector();
        }

        @Override
        @ForceInline
        Short256Vector toBitsVector() {
            return (Short256Vector) super.toBitsVectorTemplate();
        }

        @Override
        Short256Vector toBitsVector0() {
            return ((Short256Vector) vspecies().asIntegral().dummyVector()).vectorFactory(indices());
        }

        @Override
        @ForceInline
        public int laneSource(int i) {
            return (int)toBitsVector().lane(i);
        }

        @Override
        @ForceInline
        public void intoArray(int[] a, int offset) {
            VectorSpecies<Integer> species = IntVector.SPECIES_256;
            Vector<Short> v = toBitsVector();
            v.convertShape(VectorOperators.S2I, species, 0)
                    .reinterpretAsInts()
                    .intoArray(a, offset);
            v.convertShape(VectorOperators.S2I, species, 1)
                    .reinterpretAsInts()
                    .intoArray(a, offset + species.length());
        }

        @Override
        @ForceInline
        public void intoMemorySegment(MemorySegment ms, long offset, ByteOrder bo) {
            VectorSpecies<Integer> species = IntVector.SPECIES_256;
            Vector<Short> v = toBitsVector();
            v.convertShape(VectorOperators.S2I, species, 0)
                    .reinterpretAsInts()
                    .intoMemorySegment(ms, offset, bo);
            v.convertShape(VectorOperators.S2I, species, 1)
                    .reinterpretAsInts()
                    .intoMemorySegment(ms, offset + species.vectorByteSize(), bo);
         }

        @Override
        @ForceInline
        public final Short256Mask laneIsValid() {
            return (Short256Mask) toBitsVector().compare(VectorOperators.GE, 0)
                    .cast(vspecies());
        }

        @ForceInline
        @Override
        public final Short256Shuffle rearrange(VectorShuffle<Short> shuffle) {
            Short256Shuffle concreteShuffle = (Short256Shuffle) shuffle;
            return (Short256Shuffle) toBitsVector().rearrange(concreteShuffle)
                    .toShuffle(vspecies(), false);
        }

        @ForceInline
        @Override
        public final Short256Shuffle wrapIndexes() {
            Short256Vector v = toBitsVector();
            if ((length() & (length() - 1)) == 0) {
                v = (Short256Vector) v.lanewise(VectorOperators.AND, length() - 1);
            } else {
                v = (Short256Vector) v.blend(v.lanewise(VectorOperators.ADD, length()),
                            v.compare(VectorOperators.LT, 0));
            }
            return (Short256Shuffle) v.toShuffle(vspecies(), false);
        }

        private static short[] prepare(int[] indices, int offset) {
            short[] a = new short[VLENGTH];
            for (int i = 0; i < VLENGTH; i++) {
                int si = indices[offset + i];
                si = partiallyWrapIndex(si, VLENGTH);
                a[i] = (short)si;
            }
            return a;
        }

        private static short[] prepare(IntUnaryOperator f) {
            short[] a = new short[VLENGTH];
            for (int i = 0; i < VLENGTH; i++) {
                int si = f.applyAsInt(i);
                si = partiallyWrapIndex(si, VLENGTH);
                a[i] = (short)si;
            }
            return a;
        }

        private static boolean indicesInRange(short[] indices) {
            int length = indices.length;
            for (short si : indices) {
                if (si >= (short)length || si < (short)(-length)) {
                    String msg = ("index "+si+"out of range ["+length+"] in "+
                                  java.util.Arrays.toString(indices));
                    throw new AssertionError(msg);
                }
            }
            return true;
        }
    }

    // ================================================

    // Specialized low-level memory operations.

    @ForceInline
    @Override
    final
    ShortVector fromArray0(short[] a, int offset) {
        return super.fromArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    ShortVector fromArray0(short[] a, int offset, VectorMask<Short> m, int offsetInRange) {
        return super.fromArray0Template(Short256Mask.class, a, offset, (Short256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    ShortVector fromArray0(short[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Short> m) {
        return super.fromArray0Template(Short256Mask.class, a, offset, indexMap, mapOffset, (Short256Mask) m);
    }

    @ForceInline
    @Override
    final
    ShortVector fromCharArray0(char[] a, int offset) {
        return super.fromCharArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    ShortVector fromCharArray0(char[] a, int offset, VectorMask<Short> m, int offsetInRange) {
        return super.fromCharArray0Template(Short256Mask.class, a, offset, (Short256Mask) m, offsetInRange);  // specialize
    }


    @ForceInline
    @Override
    final
    ShortVector fromMemorySegment0(MemorySegment ms, long offset) {
        return super.fromMemorySegment0Template(ms, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    ShortVector fromMemorySegment0(MemorySegment ms, long offset, VectorMask<Short> m, int offsetInRange) {
        return super.fromMemorySegment0Template(Short256Mask.class, ms, offset, (Short256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(short[] a, int offset) {
        super.intoArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(short[] a, int offset, VectorMask<Short> m) {
        super.intoArray0Template(Short256Mask.class, a, offset, (Short256Mask) m);
    }



    @ForceInline
    @Override
    final
    void intoMemorySegment0(MemorySegment ms, long offset, VectorMask<Short> m) {
        super.intoMemorySegment0Template(Short256Mask.class, ms, offset, (Short256Mask) m);
    }

    @ForceInline
    @Override
    final
    void intoCharArray0(char[] a, int offset, VectorMask<Short> m) {
        super.intoCharArray0Template(Short256Mask.class, a, offset, (Short256Mask) m);
    }

    // End of specialized low-level memory operations.

    // ================================================

}

